package com.liminghuang.injector.compiler;

import com.squareup.javapoet.ClassName;

/**
 * ProjectName: AOP
 * Description:
 * CreateDate: 2020/6/10 4:01 PM
 *
 * @author: <a href="mailto:1569642270@qq.com"">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class InjectTypeName {
    public static final String CLZ_NAME_SUFFIX = "$$Injector";
    public static final ClassName CLZ_PROCEEDING_JOIN_POINT = ClassName.get("org.aspectj.lang", "ProceedingJoinPoint");
    public static final ClassName CLZ_JOIN_POINT = ClassName.get("org.aspectj.lang", "JoinPoint");
    public static final ClassName CLZ_INTENT = ClassName.get("android.content", "Intent");
    public static final ClassName CLZ_BUNDLE = ClassName.get("android.os", "Bundle");
    public static final ClassName CLZ_THROWABLE = ClassName.get("java.lang", "Throwable");
    public static final ClassName CLZ_EXCEPTION = ClassName.get("java.lang", "Exception");
}
