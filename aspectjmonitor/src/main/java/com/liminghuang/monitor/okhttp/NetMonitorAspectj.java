package com.liminghuang.monitor.okhttp;


import com.liminghuang.monitor.okhttp.aop.HttpEventListener;
import com.liminghuang.monitor.okhttp.aop.NetWorkInterceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import okhttp3.OkHttpClient;


/**
 * Aop形式
 */

@Aspect
public class NetMonitorAspectj {
    public static final String TAG = "NetMonitorAspectj";

    @Pointcut("call(public okhttp3.OkHttpClient build())")
    public void build() {}


    @Around("build()")
    public Object aroundBuild(ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();

        if (target instanceof OkHttpClient.Builder) {
            OkHttpClient.Builder builder = (OkHttpClient.Builder) target;
            builder.addInterceptor(new NetWorkInterceptor());
            builder.eventListenerFactory(HttpEventListener.FACTORY);
        }
        return joinPoint.proceed();
    }




}
