package com.liminghuang.route.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.liminghuang.route.Constants.NATIVE_SCHEME;

/**
 * Description: 路由页面注解.
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
public @interface RouteTarget {
    /**
     * path
     *
     * @return
     */
    String target() default "";

    /**
     * 路由映射键
     *
     * @return
     */
    String tag() default "";

    /**
     * authority或者domain
     *
     * @return
     */
    String domain() default "";

    /**
     * 协议
     *
     * @return
     */
    String scheme() default NATIVE_SCHEME;

    /**
     * 是否需要登录
     *
     * @return
     */
    boolean needLogin() default false;
}
