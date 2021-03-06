package com.liminghuang.viewfinder.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Description: 注解处理.
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
class AnnotatedClass {
    private TypeElement mClassElement;
    private Elements mElementUtils;
    private List<BindViewField> mFields;
    private List<OnClickMethod> mMethods;

    AnnotatedClass(TypeElement classElement, Elements elementUtils) {
        this.mClassElement = classElement;
        this.mElementUtils = elementUtils;
    }

    void addField(BindViewField bindViewField) {
        if (mFields == null) {
            mFields = new ArrayList<>();
        }
        mFields.add(bindViewField);
    }

    void addMethod(OnClickMethod onClickMethod) {
        if (mMethods == null) {
            mMethods = new ArrayList<>();
        }
        mMethods.add(onClickMethod);
    }

    Object getFullClassName() {
        return mClassElement.getQualifiedName().toString();
    }

    /**
     * 生成查找器.
     *
     * @return
     */
    JavaFile generateFinder() {

        // method inject(final T host, Object source, Provider provider)
        MethodSpec.Builder injectMethodBuilder = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addJavadoc("This method is generated by apt at $S, do not edit.",
                        new Date().toString())
                .addParameter(TypeName.get(mClassElement.asType()), "host", Modifier.FINAL)
                .addParameter(TypeName.OBJECT, "source")
                .addParameter(TypeUtil.PROVIDER, "provider");

        injectMethodBuilder.addComment("Here is bind view.");
        // find views. 查找BindView注解标注value对应的视图
        for (BindViewField field : mFields) {
            injectMethodBuilder.addStatement("host.$N = ($T)(provider.findView(source, $L))", field.getFieldName(),
                    ClassName.get(field.getFieldType()), field.getResId());
        }

        if (mMethods.size() > 0) {
            injectMethodBuilder.addComment("Here is set OnClickListener.");
            injectMethodBuilder.addStatement("$T listener", TypeUtil.ANDROID_ON_CLICK_LISTENER);
        }
        for (OnClickMethod method : mMethods) {
            // 匿名内部类的回调方法
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("onClick")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addParameter(TypeUtil.ANDROID_VIEW, "view");
            // 调用对应的点击事件
            if (method.isParametersWithView()) {
                methodBuilder.addStatement("host.$N(view)", method.getMethodName());
            } else {
                methodBuilder.addStatement("host.$N()", method.getMethodName());
            }

            // declare OnClickListener anonymous class
            TypeSpec listener = TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(TypeUtil.ANDROID_ON_CLICK_LISTENER)
                    .addMethod(methodBuilder.build())
                    .build();
            injectMethodBuilder.addStatement("listener = $L ", listener);

            // set listeners. 根据OnClick注解标注的value数组，给对应的视图设置点击监听
            for (int id : method.mClickIds) {
                injectMethodBuilder.addStatement("provider.findView(source, $L).setOnClickListener(listener)", id);
            }
        }

        // generate whole class
        TypeSpec finderClass = TypeSpec.classBuilder(mClassElement.getSimpleName() + "$$Finder")
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("This class is generated by apt at $S, do not edit.",
                        new Date().toString())
                .addSuperinterface(ParameterizedTypeName.get(TypeUtil.FINDER, TypeName.get(mClassElement.asType())))
                .addMethod(injectMethodBuilder.build())
                .build();

        String packageName = mElementUtils.getPackageOf(mClassElement).getQualifiedName().toString();
        // generate file
        return JavaFile.builder(packageName, finderClass).build();
    }
}
