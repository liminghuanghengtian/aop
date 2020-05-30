package com.liminghuang.route.model;

import com.liminghuang.route.annotation.RouteTarget;
import com.liminghuang.route.bean.RuleInfo;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;

/**
 * Description: {@link RouteTarget}解析类.
 *
 * @author <a href="mailto:huanglm@guahao.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class RouteTargetAnnotatedClass {
    private TypeElement mClassElement;
    private RuleInfo mRuleInfo;

    public RouteTargetAnnotatedClass(Element element, String domain, Elements elementUtils,
                                     Messager messager) throws IllegalArgumentException {
        // 校验元素类型
        if (element.getKind() != ElementKind.CLASS) {
            String errorMsg = String.format("Only class can be annotated with @%s", RouteTarget.class.getSimpleName());
            messager.printMessage(Kind.ERROR, errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        mClassElement = (TypeElement) element;
        RouteTarget routeTargetAnn = mClassElement.getAnnotation(RouteTarget.class);
        mRuleInfo = new RuleInfo();
        mRuleInfo.setKey(routeTargetAnn.tag());
        mRuleInfo.setPackName(elementUtils.getPackageOf(mClassElement).getQualifiedName().toString());
        mRuleInfo.setQualified(mClassElement.getQualifiedName().toString());
        mRuleInfo.setScheme(routeTargetAnn.scheme());
        mRuleInfo.setDomain(isDomainEmpty(routeTargetAnn.domain()) ? domain :
                routeTargetAnn.domain());
        mRuleInfo.setPath(routeTargetAnn.target());
        messager.printMessage(Kind.NOTE, String.format("made RuleInfo -> %s",
                mRuleInfo.toString()));
    }

    public TypeElement getClassElement() {
        return mClassElement;
    }

    public RuleInfo getRuleInfo() {
        return mRuleInfo;
    }

    private boolean isDomainEmpty(String domain) {
        return domain == null || domain.length() == 0;
    }
}
