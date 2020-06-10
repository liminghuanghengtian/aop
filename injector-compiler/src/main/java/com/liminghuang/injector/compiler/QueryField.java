package com.liminghuang.injector.compiler;


import com.liminghuang.route.inject.annotation.Query;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

/**
 * Description: Query注解内容.
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
class QueryField {
    private VariableElement mFieldElement;
    private TypeElement mClzElement;
    private Types mTypeUtils;
    private String mKey;
    private String mClzName;

    QueryField(Element element, Types typeUtils) throws IllegalArgumentException {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(
                    String.format("Only fields can be annotated with @%s", Query.class.getSimpleName()));
        }

        mFieldElement = (VariableElement) element;
        Query query = mFieldElement.getAnnotation(Query.class);
        mKey = query.key();
        mClzElement = (TypeElement) mFieldElement.getEnclosingElement();
        mClzName = mClzElement.getSimpleName().toString();
        mTypeUtils = typeUtils;
        // 返回直接超类
        TypeMirror typeMirror = mClzElement.getSuperclass();
        if (TypeKind.DECLARED.equals(typeMirror.getKind())) {
            // TODO: 2020/6/10 类或接口
        }
    }

    Name getFieldName() {
        return mFieldElement.getSimpleName();
    }

    TypeMirror getFieldType() {
        return mFieldElement.asType();
    }

    String getFieldTypeSimpleName() {
        return mTypeUtils.asElement(getFieldType()).getSimpleName().toString();
        // if (getFieldType().getKind().isPrimitive()) {
        //     return getSimpleName(getFieldType().toString());
        // } else if (getFieldType().getKind().equals(TypeKind.ARRAY)) {
        //     return getSimpleName(getFieldType().toString()) + "Array";
        // }
        // return "";
    }

    String getKey() {
        return mKey;
    }

    String getClzName() {
        return mClzName;
    }

    private String getSimpleName(String canonicalName) {
        System.out.println("canonicalName: " + canonicalName);
        if (canonicalName != null) {
            return canonicalName.substring(canonicalName.lastIndexOf("."));
        }
        return "";
    }
}
