package com.liminghuang.route.gen;

import com.liminghuang.route.abstraction.collector.IRouteCollector;
import com.liminghuang.route.model.RouteModuleAnnotatedClass;
import com.liminghuang.route.model.RouteTargetAnnotatedClass;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;

/**
 * Description: 路由表生成器.
 *
 * @author <a href="mailto:huanglm@guahao.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class RouteTableGenerator {
    private RouteModuleAnnotatedClass moduleAnnClz;
    private List<RouteTargetAnnotatedClass> targetAnnClzs;
    private Elements elementUtils;
    private Messager messager;

    public RouteTableGenerator(RouteModuleAnnotatedClass moduleAnnClz,
                               List<RouteTargetAnnotatedClass> targetAnnClzs,
                               Elements elementUtils, Messager messager) {
        this.moduleAnnClz = moduleAnnClz;
        this.targetAnnClzs = targetAnnClzs;
        this.elementUtils = elementUtils;
        this.messager = messager;
    }

    public JavaFile generate() {
        AtomicInteger order = new AtomicInteger(0);
        ClassName routeRuleClzName = ClassName.get("com.liminghuang.route", Types.ROUTE_RULE);
        ClassName routeRuleBuilderClzName = ClassName.get("com.liminghuang.route",
                Types.ROUTE_RULE_BUILDER);
        ClassName listClzName = ClassName.get("java.util", "List");
        ClassName arrayListClzName = ClassName.get("java.util", "ArrayList");
        TypeName listOfRouteRule = ParameterizedTypeName.get(listClzName, routeRuleClzName);
        ClassName iRouteCollectorClzName = ClassName.get(IRouteCollector.class);

        // generate method
        messager.printMessage(Kind.NOTE, "start to generate method-{collectRules}");
        MethodSpec.Builder methodCollectRulesBuilder = MethodSpec.methodBuilder("collectRules")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(listOfRouteRule)
                .addJavadoc("Here's the codes are generated by apt at $S, do not edit.",
                        new Date().toString());
        // 创建路由列表
        methodCollectRulesBuilder.addStatement("$T rules = new $T<>()", listOfRouteRule,
                arrayListClzName);
        // 添加路由规则
        for (RouteTargetAnnotatedClass target : targetAnnClzs) {
            CodeBlock.Builder ruleBuilder = CodeBlock.builder();
            int seq = order.incrementAndGet();
            ruleBuilder.addStatement("$T builder$L = new $T()", routeRuleBuilderClzName, seq,
                    routeRuleBuilderClzName);
            ruleBuilder.addStatement("builder$L.setScheme($S)", seq, target.getRuleInfo().getScheme());
            ruleBuilder.addStatement("builder$L.setAuthority($S)", seq, target.getRuleInfo().getDomain());
            ruleBuilder.addStatement("builder$L.setPath($S)", seq, target.getRuleInfo().getPath());
            ruleBuilder.addStatement("builder$L.setKey($S)", seq, target.getRuleInfo().getKey());
            ruleBuilder.addStatement("builder$L.setQualified($S)", seq, target.getRuleInfo().getQualified());
            ruleBuilder.addStatement("rules.add(builder$L.build())", seq);
            ruleBuilder.add(CodeBlock.builder().add("/* rule$L added into rules*/\n", seq).build());
            methodCollectRulesBuilder.addCode(ruleBuilder.build());
        }
        methodCollectRulesBuilder.addStatement("return rules");

        // generate whole class
        String generateClzName = "RouteCollector_" + moduleAnnClz.getModuleInfo().getDomain();
        messager.printMessage(Kind.NOTE, String.format("start to generate class-{%s}",
                generateClzName));
        TypeSpec collectorClz =
                TypeSpec.classBuilder(generateClzName)
                        .addModifiers(Modifier.PUBLIC)
                        .addSuperinterface(iRouteCollectorClzName)
                        .addJavadoc("This class is generated by apt at $S, do not edit.",
                                new Date().toString())
                        .addMethod(methodCollectRulesBuilder.build())
                        .build();

        String packageName = elementUtils.getPackageOf(moduleAnnClz.getClassElement()).getQualifiedName().toString();
        messager.printMessage(Kind.NOTE, String.format("class-{%s} will be generate at(pkg same " +
                        "as %s): %s",
                generateClzName, moduleAnnClz.getModuleInfo().getSimpleName(), packageName));
        // generate file
        return JavaFile.builder(packageName, collectorClz).build();
    }
}
