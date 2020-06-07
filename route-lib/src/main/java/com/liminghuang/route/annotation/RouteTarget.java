package com.liminghuang.route.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.liminghuang.route.Constants.NATIVE_SCHEME;

/**
 * Description:
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
public @interface RouteTarget {
    String target() default "";

    String tag() default "";

    String domain() default "";

    String scheme() default NATIVE_SCHEME;

    boolean needLogin() default false;
}
