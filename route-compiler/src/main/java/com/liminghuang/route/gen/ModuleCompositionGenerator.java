package com.liminghuang.route.gen;

import com.liminghuang.route.processor.RouterProcessor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * ProjectName: AOP
 * Description: app主模块进行下述合成.
 * CreateDate: 2020/6/10 8:51 PM
 *
 * @author: <a href="mailto:1569642270@qq.com">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class ModuleCompositionGenerator {
    private Elements elementUtils;
    private Messager messager;
    private String outputPath;

    public ModuleCompositionGenerator(String outputPath,
                                      Elements elementUtils, Messager messager) {
        this.outputPath = outputPath;
        this.elementUtils = elementUtils;
        this.messager = messager;
    }

    private void genReflect(MethodSpec.Builder builder) {
        if (outputPath == null || outputPath.length() == 0) {
            return;
        }

        File routerDir = new File(outputPath+ RouterProcessor.ROUTE_DIR);
        if (routerDir.exists() && routerDir.isDirectory()) {
            messager.printMessage(Diagnostic.Kind.NOTE, String.format("%s is directory", routerDir.getAbsolutePath()));
            File[] fileList = routerDir.listFiles();
            if (fileList != null) {
                builder.addStatement("$T module", Types.CLZ_IROUTE_MODULE);
                for (File f : fileList) {
                    messager.printMessage(Diagnostic.Kind.NOTE, String.format("filename: %s", f.getName()));
                    String fileName = f.getName();
                    String canonicalModuleClzName = fileName.substring(0, fileName.lastIndexOf("."));
                    messager.printMessage(Diagnostic.Kind.NOTE, String.format("ModuleClzName: %s", canonicalModuleClzName));
                    String pkg = canonicalModuleClzName.substring(0, canonicalModuleClzName.lastIndexOf("."));
                    String clz = canonicalModuleClzName.substring(canonicalModuleClzName.lastIndexOf(".") + 1);
                    messager.printMessage(Diagnostic.Kind.NOTE, String.format("ModuleClzName pkg: %s, clz: %s",
                            pkg, clz));
                    String canonicalModuleProxyClzName =
                            fileName.substring(0, fileName.lastIndexOf(".")) + Types.ROUTE_MODULE_SUFFIX;
                    messager.printMessage(Diagnostic.Kind.NOTE, String.format("ModuleProxyClzName: %s", canonicalModuleProxyClzName));

                    builder.addComment("composite $S", clz);
                    builder.addCode("try {\n")
                            .addStatement("\t$T clz = $T.forName($S)", Types.CLZ_CLASS, Types.CLZ_CLASS,
                                    canonicalModuleProxyClzName)
                            .addStatement("\t$T c = clz.getConstructor(IRouteModule.class)", Types.CLZ_CONSTRUCT)
                            .addStatement("\tmodule = ($T) c.newInstance($T.forName($S).newInstance())",
                                    Types.CLZ_IROUTE_MODULE, Types.CLZ_CLASS, canonicalModuleClzName)
                            .addStatement("\tmodules.add(module)")
                            .addCode("} catch($T | $T | $T | $T | $T e) {\n",
                                    ClassName.get(InstantiationException.class),
                                    ClassName.get(IllegalAccessException.class),
                                    ClassName.get(ClassNotFoundException.class),
                                    ClassName.get(NoSuchMethodException.class),
                                    ClassName.get(InvocationTargetException.class))
                            .addStatement("\te.printStackTrace()")
                            .addCode("}\n");
                }
            }
        }
    }

    public JavaFile generate() {
        // getModules
        MethodSpec.Builder getModulesBuilder = MethodSpec.methodBuilder("getModules");
        getModulesBuilder.addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(Types.TYPE_LIST_OF_MODULE)
                .addJavadoc("This method is generated by apt at $S, do not edit.",
                        new Date().toString())
                .addStatement("$T modules = new $T<>()", Types.TYPE_LIST_OF_MODULE, Types.CLZ_ARRAY_LIST);
        genReflect(getModulesBuilder);
        getModulesBuilder.addStatement("return modules");

        // generate whole class
        String generateClzName = Types.ROUTE_MODULE_COMPOSITION_CLZ_NAME;
        messager.printMessage(Diagnostic.Kind.NOTE, String.format("start to generate class-{%s}",
                generateClzName));
        TypeSpec moduleCompositionClz =
                TypeSpec.classBuilder(generateClzName)
                        .addModifiers(Modifier.PUBLIC)
                        .addSuperinterface(Types.CLZ_IMODULE_COMPOSITION)
                        .addJavadoc("This class is generated by apt at $S, do not edit.",
                                new Date().toString())
                        .addMethod(getModulesBuilder.build())
                        .build();

        messager.printMessage(Diagnostic.Kind.NOTE, String.format("class-{%s} will be generate at: %s",
                generateClzName, Types.ROUTE_PKG));
        // generate file
        return JavaFile.builder(Types.ROUTE_PKG, moduleCompositionClz).build();
    }
}
