package com.liminghuang.log.plugin

import com.android.annotations.NonNull
import com.android.build.api.transform.*
import com.android.build.api.transform.QualifiedContent.ContentType
import com.android.build.api.transform.QualifiedContent.Scope
import com.google.common.collect.Sets
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import javassist.ClassPool

import java.util.function.Consumer

/**
 * Transform
 */
public class LogTransform extends Transform {
    Project project;

    public LogTransform(Project project) {
        this.project = project;
    }

    /**
     * 自定义Transform对应的名称
     *
     * @return
     */
    @Override
    public String getName() {
        return "LogTask";
    }

    /**
     * 指定输入类型，只处理这一类的文件.
     *
     * @return
     */
    @Override
    public Set<ContentType> getInputTypes() {
        // return Sets.immutableEnumSet(QualifiedContent.DefaultContentType.CLASSES);
        Set<ContentType> set = new HashSet<>();
        set.add(QualifiedContent.DefaultContentType.CLASSES);
        return set;
    }

    /**
     * transform作用范围.
     *
     * @return
     */
    @Override
    public Set<? super Scope> getScopes() {
        return Sets.immutableEnumSet(Scope.PROJECT,
                Scope.PROJECT_LOCAL_DEPS,
                Scope.SUB_PROJECTS,
                Scope.SUB_PROJECTS_LOCAL_DEPS,
                Scope.EXTERNAL_LIBRARIES);
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(@NonNull TransformInvocation transformInvocation)
            throws TransformException, InterruptedException, IOException {
        // Just delegate to old method, for code that uses the old API.
        //noinspection deprecation
        // transform(transformInvocation.getContext(), transformInvocation.getInputs(),
        //         transformInvocation.getReferencedInputs(),
        //         transformInvocation.getOutputProvider(),
        //         transformInvocation.isIncremental());

        def startTime = System.currentTimeMillis();
        // inputs有两种类型，目录和jar包要分开处理
        transformInvocation.getInputs().forEach(new Consumer<TransformInput>() {
            @Override
            public void accept(TransformInput transformInput) {
                try {
                    // 遍历jar包
                    transformInput.getJarInputs().forEach(new Consumer<JarInput>() {
                        @Override
                        public void accept(JarInput jarInput) {
                            project.logger.debug jarInput.name

                            MyInject.injectDir(jarInput.file.getAbsolutePath(), "com.liminghuang.demo", project)
                            String outputFileName = jarInput.name.replace(".jar", "") + '-' + jarInput.file.path.hashCode()
                            def output = transformInvocation.getOutputProvider().getContentLocation(outputFileName, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                            FileUtils.copyFile(jarInput.file, output)
                        }
                    });
                } catch (Exception e) {
                    project.logger.err e.getMessage()
                };

                // 遍历文件
                transformInput.getDirectoryInputs().forEach(new Consumer<DirectoryInput>() {
                    @Override
                    public void accept(DirectoryInput directoryInput) {
                        project.logger.debug directoryInput.name

                        // 文件夹里面包含的是我们手写的类以及R.class、BuildConfig.class以及R$XXX.class等
                        MyInject.injectDir(directoryInput.file.absolutePath, "com.liminghuang.demo", project)
                        // 获取output目录
                        def dest = transformInvocation.getOutputProvider().getContentLocation(directoryInput.name,
                                directoryInput.contentTypes, directoryInput.scopes,
                                Format.DIRECTORY)
                        // 将input的目录复制到output指定目录
                        FileUtils.copyDirectory(directoryInput.file, dest)
                    }
                });
            }
        });

        ClassPool.getDefault().clearImportedPackages();
        project.logger.error("JavassistTransform cast :" + (System.currentTimeMillis() - startTime) / 1000 + " secs");
    }
}
