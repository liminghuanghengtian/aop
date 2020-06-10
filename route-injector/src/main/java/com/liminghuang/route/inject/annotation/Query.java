package com.liminghuang.route.inject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ProjectName: AOP
 * Description: 路由参数注解.
 * CreateDate: 2020/6/10 2:22 PM
 *
 * @author: <a href="mailto:1569642270@qq.com"">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD})
public @interface Query {
    String key() default "";
}
