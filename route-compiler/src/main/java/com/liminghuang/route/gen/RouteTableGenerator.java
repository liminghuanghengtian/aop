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
        ClassName listClzName = ClassName.get("java.util", "List");
        ClassName arrayListClzName = ClassName.get("java.util", "ArrayList");
        TypeName listOfRouteRule = ParameterizedTypeName.get(listClzName, routeRuleClzName);
        ClassName IRouteCollectorClzName = ClassName.get(IRouteCollector.class);

        // generate method
        messager.printMessage(Kind.NOTE, "start to generate method-{collectRules}");
        MethodSpec.Builder methodCollectRulesBuilder = MethodSpec.methodBuilder("collectRules")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(listOfRouteRule)
                .addJavadoc("Here's the code were generated by apt");
        methodCollectRulesBuilder.addStatement("$T rules = new $T<>()", listOfRouteRule,
                arrayListClzName);
        for (RouteTargetAnnotatedClass target : targetAnnClzs) {
            CodeBlock.Builder ruleBuilder = CodeBlock.builder();
            int seq = order.incrementAndGet();
            ruleBuilder.addStatement("$T rule$L = new $T()", routeRuleClzName, seq, routeRuleClzName);
            ruleBuilder.addStatement("rule$L.setScheme($S)", seq, target.getRuleInfo().getScheme());
            ruleBuilder.addStatement("rule$L.setDomain($S)", seq, target.getRuleInfo().getDomain());
            ruleBuilder.addStatement("rule$L.setQualified($S)", seq, target.getRuleInfo().getQualified());
            ruleBuilder.addStatement("rule$L.setPath($S)", seq, target.getRuleInfo().getPath());
            ruleBuilder.addStatement("rule$L.setKey($S)", seq, target.getRuleInfo().getKey());
            ruleBuilder.addStatement("rules.add(rule$L)", seq);
            ruleBuilder.add(CodeBlock.builder().add("/* rule$L added */\n", seq).build());
            methodCollectRulesBuilder.addCode(ruleBuilder.build());
        }
        methodCollectRulesBuilder.addStatement("return rules");

        // generate whole class
        String generateClzName = "Collector_" + moduleAnnClz.getModuleInfo().getDomain();
        messager.printMessage(Kind.NOTE, String.format("start to generate class-{%s}",
                generateClzName));
        TypeSpec collectorClz =
                TypeSpec.classBuilder(generateClzName)
                        .addModifiers(Modifier.PUBLIC)
                        .addSuperinterface(IRouteCollectorClzName)
                        .addMethod(methodCollectRulesBuilder.build())
                        .build();

        String packageName = elementUtils.getPackageOf(moduleAnnClz.getClassElement()).getQualifiedName().toString();
        messager.printMessage(Kind.NOTE, String.format("start to generate class-{%s}",
                generateClzName));
        messager.printMessage(Kind.NOTE, String.format("class-{%s} will be generate at(pkg same " +
                        "as %s): %s",
                generateClzName, moduleAnnClz.getModuleInfo().getSimpleName(), packageName));
        // generate file
        return JavaFile.builder(packageName, collectorClz).build();
    }
}
