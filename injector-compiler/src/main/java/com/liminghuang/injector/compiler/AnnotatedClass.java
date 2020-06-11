package com.liminghuang.injector.compiler;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * ProjectName: AOP
 * Description:
 * CreateDate: 2020/6/10 3:49 PM
 *
 * @author: <a href="mailto:1569642270@qq.com"">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class AnnotatedClass {
    private TypeElement mClassElement;
    private Elements mElementUtils;
    private List<QueryField> mFields;


    AnnotatedClass(TypeElement classElement, Elements elementUtils) {
        this.mClassElement = classElement;
        this.mElementUtils = elementUtils;
    }

    void addField(QueryField queryField) {
        if (mFields == null) {
            mFields = new ArrayList<>();
        }
        mFields.add(queryField);
    }

    String getFullClassName() {
        return mClassElement.getQualifiedName().toString();
    }

    MethodSpec.Builder makeMethodOnCreate() {
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder().add("$S", "execution(* *.onCreate(..))");
        AnnotationSpec annotationSpec = AnnotationSpec.builder(Around.class)
                .addMember("value", codeBlockBuilder.build()).build();
        MethodSpec.Builder onCreateMethodBuilder = MethodSpec.methodBuilder("onCreate")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(annotationSpec)
                .addException(InjectTypeName.CLZ_THROWABLE)
                .addParameter(InjectTypeName.CLZ_PROCEEDING_JOIN_POINT, "joinPoint")
                .addJavadoc("This method is generated by apt at $S, don't modify it", new Date().toString());

        onCreateMethodBuilder.addStatement("$T target = ($T)joinPoint.getTarget()", ClassName.get(mClassElement),
                ClassName.get(mClassElement));
        onCreateMethodBuilder.addStatement("$T dataBundle = new $T()", InjectTypeName.CLZ_BUNDLE, InjectTypeName.CLZ_BUNDLE);
        onCreateMethodBuilder.addStatement("$T saveBundle = ($T)joinPoint.getArgs()[0]", InjectTypeName.CLZ_BUNDLE,
                InjectTypeName.CLZ_BUNDLE);
        onCreateMethodBuilder.addStatement("$T targetBundle = target.getIntent().getExtras()", InjectTypeName.CLZ_BUNDLE);
        onCreateMethodBuilder.addStatement("if(targetBundle != null) {")
                .addStatement("dataBundle.putAll(targetBundle)")
                .addStatement("}");
        onCreateMethodBuilder.addStatement("if(saveBundle != null) {")
                .addStatement("dataBundle.putAll(saveBundle)")
                .addStatement("}");
        onCreateMethodBuilder.addStatement("try {");
        for (QueryField queryField : mFields) {
            onCreateMethodBuilder.addStatement("target.$N = dataBundle.get$L($S)", queryField.getFieldName(),
                    queryField.getFieldTypeSimpleName(),
                    queryField.getKey());
        }
        onCreateMethodBuilder.addStatement("} catch($T e) {", InjectTypeName.CLZ_EXCEPTION)
                .addStatement("e.printStackTrace()")
                .addStatement("}");
        onCreateMethodBuilder.addStatement("joinPoint.proceed()");
        return onCreateMethodBuilder;
    }

    MethodSpec.Builder makeMethodOnSaveInstanceState() {
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder().add("$S", "execution(* *.onSaveInstanceState(..))");
        AnnotationSpec annotationSpec = AnnotationSpec.builder(After.class)
                .addMember("value", codeBlockBuilder.build()).build();
        MethodSpec.Builder onSaveInstanceStateMethodBuilder = MethodSpec.methodBuilder("onSaveInstanceState")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(annotationSpec)
                .addException(InjectTypeName.CLZ_THROWABLE)
                .addParameter(InjectTypeName.CLZ_JOIN_POINT, "joinPoint")
                .addJavadoc("This method is generated by apt at $S, don't modify it", new Date().toString());

        onSaveInstanceStateMethodBuilder.addStatement("$T target = ($T)joinPoint.getTarget()", ClassName.get(mClassElement),
                ClassName.get(mClassElement));
        onSaveInstanceStateMethodBuilder.addStatement("$T saveBundle = ($T)joinPoint.getArgs()[0]", InjectTypeName.CLZ_BUNDLE,
                InjectTypeName.CLZ_BUNDLE);
        onSaveInstanceStateMethodBuilder.addStatement("$T intent = new $T()", InjectTypeName.CLZ_INTENT,
                InjectTypeName.CLZ_INTENT);
        for (QueryField queryField : mFields) {
            // String type = queryField.getFieldType().toString();
            onSaveInstanceStateMethodBuilder.addStatement("intent.putExtra($S, target.$N)",
                    queryField.getKey(), queryField.getFieldName());
        }
        onSaveInstanceStateMethodBuilder.addStatement("saveBundle.putAll(intent.getExtras())");
        return onSaveInstanceStateMethodBuilder;
    }

    MethodSpec.Builder makeMethodOnNewIntent() {
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder().add("$S", "execution(* *.onNewIntent(..))");
        AnnotationSpec annotationSpec = AnnotationSpec.builder(Around.class)
                .addMember("value", codeBlockBuilder.build()).build();
        MethodSpec.Builder onNewIntentMethodBuilder = MethodSpec.methodBuilder("onNewIntent")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(annotationSpec)
                .addException(InjectTypeName.CLZ_THROWABLE)
                .addParameter(InjectTypeName.CLZ_PROCEEDING_JOIN_POINT, "joinPoint")
                .addJavadoc("This method is generated by apt at $S, don't modify it", new Date().toString());

        onNewIntentMethodBuilder.addStatement("$T target = ($T)joinPoint.getTarget()", ClassName.get(mClassElement),
                ClassName.get(mClassElement));
        onNewIntentMethodBuilder.addStatement("$T targetIntent = ($T)joinPoint.getArgs()[0]", InjectTypeName.CLZ_INTENT,
                InjectTypeName.CLZ_INTENT);
        onNewIntentMethodBuilder.addStatement("$T dataBundle = targetIntent.getExtras()", InjectTypeName.CLZ_BUNDLE);
        onNewIntentMethodBuilder.addStatement("try {");
        for (QueryField queryField : mFields) {
            onNewIntentMethodBuilder.addStatement("target.$N = dataBundle.get$L($S)", queryField.getFieldName(),
                    queryField.getFieldTypeSimpleName(),
                    queryField.getKey());
        }
        onNewIntentMethodBuilder.addStatement("} catch($T e) {", InjectTypeName.CLZ_EXCEPTION)
                .addStatement("e.printStackTrace()")
                .addStatement("}");
        onNewIntentMethodBuilder.addStatement("joinPoint.proceed()");
        return onNewIntentMethodBuilder;
    }

    /**
     * 生成查找器.
     *
     * @return
     */
    JavaFile generateInjector() {
        // generate whole class
        TypeSpec injectClass = TypeSpec.classBuilder(mClassElement.getSimpleName() + "$$Injector")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Aspect.class)
                .addJavadoc("This class is generated by apt at $S, do not edit.",
                        new Date().toString())
                .addMethod(makeMethodOnCreate().build())
                .addMethod(makeMethodOnSaveInstanceState().build())
                .addMethod(makeMethodOnNewIntent().build())
                .build();

        String packageName = mElementUtils.getPackageOf(mClassElement).getQualifiedName().toString();
        // generate file
        return JavaFile.builder(packageName, injectClass).build();
    }
}