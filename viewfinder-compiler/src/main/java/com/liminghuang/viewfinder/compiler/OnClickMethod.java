package com.liminghuang.viewfinder.compiler;

import com.liminghuang.viewfinder.annotation.OnClick;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;

/**
 * Description: 方法注解内容.
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
class OnClickMethod {
    private ExecutableElement mExecutableElement;
    int[] mClickIds;

    OnClickMethod(Element element) throws IllegalArgumentException {
        if (element.getKind() != ElementKind.METHOD) {
            throw new IllegalArgumentException(
                    String.format("Only methods can be annotated with @%s",
                            OnClick.class.getSimpleName()));
        }

        mExecutableElement = (ExecutableElement) element;
        OnClick onClick = mExecutableElement.getAnnotation(OnClick.class);
        mClickIds = onClick.value();
    }

    Name getMethodName() {
        return mExecutableElement.getSimpleName();
    }

    /**
     * 参数列表中是否包含View参数.
     *
     * @return
     */
    boolean isParametersWithView() {
        boolean ret = false;
        List<? extends VariableElement> params = mExecutableElement.getParameters();
        for (VariableElement paramElement : params) {
            if (TypeUtil.ANDROID_VIEW.equals(TypeName.get(paramElement.asType()))) {
                ret = true;
                break;
            }
        }
        System.out.print(mExecutableElement.getSimpleName() + " isParametersWithView: " + ret);
        return ret;
    }
}
