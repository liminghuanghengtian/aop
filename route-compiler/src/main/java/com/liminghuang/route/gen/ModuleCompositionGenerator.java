package com.liminghuang.route.gen;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

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
 * @author: <a href="mailto:1569642270@qq.com"">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class ModuleCompositionGenerator {
    private Elements elementUtils;
    private Messager messager;

    public ModuleCompositionGenerator(
            Elements elementUtils, Messager messager) {
        this.elementUtils = elementUtils;
        this.messager = messager;
    }

    public JavaFile generate() {
        // getModules
        MethodSpec.Builder getModulesBuilder = MethodSpec.methodBuilder("getModules");
        getModulesBuilder.addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(Types.TYPE_LIST_OF_MODULE)
                .addJavadoc("This method is generated by apt at $S, do not edit.",
                        new Date().toString())
                .addStatement("$T modules = new $T<>()", Types.TYPE_LIST_OF_MODULE, Types.CLZ_ARRAY_LIST)
                .addStatement("return modules");
        // TODO: 2020/6/10  添加方法体内容

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
