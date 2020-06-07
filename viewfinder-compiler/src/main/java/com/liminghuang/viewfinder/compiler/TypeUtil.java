package com.liminghuang.viewfinder.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

/**
 * Description: 类名常量.
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
class TypeUtil {
    static final ClassName ANDROID_ON_CLICK_LISTENER = ClassName.get("android.view", "View.OnClickListener");
    static final TypeName ANDROID_VIEW = ClassName.get("android.view", "View");
    static final ClassName FINDER = ClassName.get("com.liminghuang.viewfinder", "Finder");
    static final TypeName PROVIDER = ClassName.get("com.liminghuang.viewfinder", "Provider");
}
