package com.liminghuang.viewfinder.compiler;

import com.liminghuang.viewfinder.annotation.BindView;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Description: View注解内容.
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
class BindViewField {
    private VariableElement mFieldElement;
    private int mResId;

    BindViewField(Element element) throws IllegalArgumentException {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(
                    String.format("Only fields can be annotated with @%s", BindView.class.getSimpleName()));
        }

        mFieldElement = (VariableElement) element;
        BindView bindView = mFieldElement.getAnnotation(BindView.class);
        mResId = bindView.value();
    }

    Name getFieldName() {
        return mFieldElement.getSimpleName();
    }

    TypeMirror getFieldType() {
        return mFieldElement.asType();
    }

    int getResId() {
        return mResId;
    }
}
