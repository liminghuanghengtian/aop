package com.liminghuang.cache;

import android.text.TextUtils;
import android.util.Log;

import com.liminghuang.cache.annotation.MemCache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.List;

/**
 * Description: 定义切片及处理逻辑
 *
 * @author <a href="mailto:huanglm@guahao.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Aspect
public class MemCacheAspect {
    private static final String TAG = "MemCacheAspect";
    ICache mCache = FactoryManager.getInstance().getFactory().createCache();
    /**
     * 定义methodexecution的JPoint（JoinPoint）：标注了MemCache
     * 注解，且任意返回值，且任意参数个数和类型，且任意方法名称，且非MemCacheAspect类本身，且非构造器
     * 会被切入.
     */
     /*&& !within(MemCacheAspect) " +
            "&& !withinCode(*.new(..))*/
    private static final String POINTCUT_VALUE = "execution(@com.liminghuang.cache.annotation" +
            ".MemCache * com.liminghuang.demo..*.*(..)) && @annotation(ann)";

    /**
     * 切入点：告诉代码注入工具，在何处注入一段特定代码的表达式.
     */
    @Pointcut(POINTCUT_VALUE)
    public void methodAnnotated(MemCache ann) {

    }

    @After("methodAnnotated(ann)")
    public void afterJoinPoint(ProceedingJoinPoint joinPoint, MemCache ann) {
        Log.d(TAG, "afterJoinPoint: " + joinPoint.toString() + ", at: " + joinPoint.getSourceLocation());
    }

    @Before("methodAnnotated(ann)")
    public void beforeJoinPoint(ProceedingJoinPoint joinPoint, MemCache ann) {
        Log.d(TAG,
                "beforeJoinPoint: " + (joinPoint != null ? joinPoint.toString() : "null") +
                        ", at: " + (joinPoint != null ? joinPoint.getSourceLocation() : "null"));
    }

    /**
     * 在连接点进行方法替换：通过around注解表示完全替代目标方法执行.
     *
     * @param joinPoint
     * @return
     */
    @Around("methodAnnotated(ann)")
    public Object aroundJoinPoint(ProceedingJoinPoint joinPoint, MemCache ann) throws Throwable {
        Log.d(TAG, "aroundJoinPoint, annotation key: " + ann.key());
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getName();
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(methodName).append("_");
        for (Object obj : joinPoint.getArgs()) {
            if (obj instanceof String) {
                keyBuilder.append(obj);
            } else if (obj instanceof Class) {
                keyBuilder.append(((Class) obj).getSimpleName());
            } else {
                keyBuilder.append(obj);
            }
            keyBuilder.append("_");
        }
        String key = keyBuilder.toString();
        Log.i(TAG, "key is {" + key + "}");
        Object result = mCache.get(key);
        Log.d(TAG, "aroundJoinPoint: key{" + key + "} with result -> " + result + " retrieved");
        if (result != null) {
            return result;
        }

        result = joinPoint.proceed();
        if (result instanceof List && result != null && ((List) result).size() > 0 // 列表不为空
                || result instanceof String && !TextUtils.isEmpty((String) result)// 字符不为空
                || result instanceof Object && result != null) {// 对象不为空
            mCache.put(key, result);
            Log.i(TAG, "aroundJoinPoint: key{" + key + "} with result -> " + result + " saved");
        }
        return result;
    }

    public static String getKey(ProceedingJoinPoint joinPoint, MemCache memCache) {
        // 获取注解参数
        String key = null;
        if (memCache != null) {
            key = memCache.key();
        }

        if (TextUtils.isEmpty(key)) {
            // 获取方法上的参数
            Object[] args = joinPoint.getArgs();
            if (args != null) {
                StringBuilder sb = new StringBuilder();
                for (Object obj : args) {
                    sb.append(obj).append("_");
                }
                key = sb.toString();
            }
        }

        Log.d(TAG, "key: " + key);
        return key;
    }
}
