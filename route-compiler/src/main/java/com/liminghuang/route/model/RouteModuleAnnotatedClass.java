package com.liminghuang.route.model;

import com.liminghuang.route.annotation.RouteModule;
import com.liminghuang.route.bean.ModuleInfo;

import java.util.Objects;

import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

/**
 * Description: {@link RouteModule}解析类.
 *
 * @author <a href="mailto:huanglm@guahao.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class RouteModuleAnnotatedClass {
    private TypeElement mClassElement;
    private ModuleInfo mModuleInfo;

    public RouteModuleAnnotatedClass(TypeElement element, Messager messager) throws IllegalArgumentException {
        mClassElement =  element;
        RouteModule routeModuleAnn = mClassElement.getAnnotation(RouteModule.class);
        mModuleInfo = new ModuleInfo();
        mModuleInfo.setQualified(mClassElement.getQualifiedName().toString());
        mModuleInfo.setSimpleName(mClassElement.getSimpleName().toString());
        mModuleInfo.setDomain(routeModuleAnn.domain());
        messager.printMessage(Kind.NOTE, String.format("made ModuleInfo -> %s",
                mModuleInfo.toString()));
    }

    public TypeElement getClassElement() {
        return mClassElement;
    }

    public ModuleInfo getModuleInfo() {
        return mModuleInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouteModuleAnnotatedClass)) {
            return false;
        }
        RouteModuleAnnotatedClass that = (RouteModuleAnnotatedClass) o;
        return mClassElement.equals(that.mClassElement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mClassElement);
    }
}
