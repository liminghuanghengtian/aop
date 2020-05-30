package com.liminghuang.route.processor;


import com.google.auto.service.AutoService;
import com.liminghuang.route.annotation.RouteModule;
import com.liminghuang.route.annotation.RouteTarget;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;

/**
 * 使用 Google 的 auto-service 库可以自动生成 META-INF/services/javax.annotation.processing.Processor 文件
 */
// @SupportedAnnotationTypes({"com.liminghuang.route.annotation.RouterTarget"})//支持的注解的完整类路径
// @SupportedSourceVersion(SourceVersion.RELEASE_8)//该处理器支持的源码版本
@AutoService(Processor.class)
public class RouteAnnProcessor extends AbstractProcessor {
    /** 文件相关辅助类 */
    private Filer mFiler;
    /** 元素相关辅助类 */
    private Elements mElementUtils;
    /** 日志相关辅助类 */
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();

        // 在这里打印gradle文件传进来的参数
        Map<String, String> map = processingEnv.getOptions();
        for (String key : map.keySet()) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, "key" + ": " + map.get(key));
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mMessager.printMessage(Kind.NOTE, String.format("annotations: %s", annotations));
        mMessager.printMessage(Kind.NOTE, String.format("locale: %s",
                processingEnv.getLocale().toString()));
        mMessager.printMessage(Kind.NOTE, String.format("sourceVersion: %s",
                processingEnv.getSourceVersion().toString()));
        processingEnv.getTypeUtils();
        mMessager.printMessage(Kind.NOTE, String.format("options: %s", processingEnv.getOptions()));
        new RouterProcessor().process(roundEnv, mFiler, mElementUtils, mMessager);
        return true;
    }

    /**
     * @return 指定哪些注解应该被注解处理器注册
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(RouteModule.class.getCanonicalName());
        types.add(RouteTarget.class.getCanonicalName());
        return types;
    }

    /**
     * @return 指定使用的 Java 版本。通常返回 SourceVersion.latestSupported()。
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
