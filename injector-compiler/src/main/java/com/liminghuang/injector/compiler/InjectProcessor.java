package com.liminghuang.injector.compiler;

import com.google.auto.service.AutoService;
import com.liminghuang.route.inject.annotation.Query;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@SupportedAnnotationTypes({"com.liminghuang.route.inject.annotation.Query"})//支持的注解的完整类路径
@SupportedSourceVersion(SourceVersion.RELEASE_8)//该处理器支持的源码版本
@AutoService(Processor.class)
public class InjectProcessor extends AbstractProcessor {
    /** 文件相关的辅助类. */
    private Filer mFiler;
    /** 元素相关的辅助类. */
    private Elements mElementUtils;
    /** 日志相关的辅助类. */
    private Messager mMessager;
    private Types mTypeUtils;
    /** 维护类对应的AnnotationedClass. */
    private Map<String, AnnotatedClass> mAnnotatedClassMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mTypeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // process() will be called several times
        mAnnotatedClassMap.clear();

        try {
            processInject(roundEnvironment);
        } catch (IllegalArgumentException e) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            // stop process
            return true;
        }

        for (AnnotatedClass annotatedClass : mAnnotatedClassMap.values()) {
            try {
                mMessager.printMessage(Diagnostic.Kind.NOTE, String.format("Generating file for %s",
                        annotatedClass.getFullClassName()));
                annotatedClass.generateInjector().writeTo(mFiler);
            } catch (IOException e) {
                mMessager.printMessage(Diagnostic.Kind.ERROR, String.format("Generate file failed, reason: %s",
                        e.getMessage()));
                return true;
            }
        }
        return true;
    }

    private void processInject(RoundEnvironment roundEnv) throws IllegalArgumentException {
        for (Element element : roundEnv.getElementsAnnotatedWith(Query.class)) {
            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            QueryField field = new QueryField(element, mTypeUtils);
            annotatedClass.addField(field);
        }
    }

    private AnnotatedClass getAnnotatedClass(Element element) {
        // 获取注解所在类的全限定路径类名
        TypeElement classElement = (TypeElement) element.getEnclosingElement();
        String fullClassName = classElement.getQualifiedName().toString();
        AnnotatedClass annotatedClass = mAnnotatedClassMap.get(fullClassName);
        if (annotatedClass == null) {
            annotatedClass = new AnnotatedClass(classElement, mElementUtils);
            mAnnotatedClassMap.put(fullClassName, annotatedClass);
        }
        return annotatedClass;
    }
}
