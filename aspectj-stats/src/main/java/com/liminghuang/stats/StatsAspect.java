package com.liminghuang.stats;

import android.util.Log;
import android.view.ViewGroup;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;

/**
 * Description: 劫持点击事件切片，生命周期回调自动打印日志.
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Aspect
public class StatsAspect {
    private static final String TAG = "StatsAspect";
    /**
     * 1. @注解 访问权限 返回值的类型 包名.函数名(参数)
     * 2.  @注解和访问权限（public/private/protect，以及static/final）属于可选项。如果不设置它们，则默认都会选择。以访问权限为例，如果没有设置访问权限作为条件，那么public，private，protect及static、final的函数都会进行搜索。
     * 3.  返回值类型就是普通的函数的返回值类型。如果不限定类型的话，就用*通配符表示
     * 4.  包名.函数名用于查找匹配的函数。可以使用通配符，包括*和..以及+号。其中*号用于匹配除.号之外的任意字符，而..则表示任意子package，+号表示子类。
     * 5.  比如：
     * 6.  java.*.Date：可以表示java.sql.Date，也可以表示java.util.Date
     * 7.  Test*：可以表示TestBase，也可以表示TestDervied
     * 8.  java..*：表示java任意子类
     * 9.  java..*Model+：表示Java任意package中名字以Model结尾的子类，比如TabelModel，TreeModel
     * 10.  等
     * 11.  最后来看函数的参数。参数匹配比较简单，主要是参数类型，比如：
     * 12.  (int, char)：表示参数只有两个，并且第一个参数类型是int，第二个参数类型是char
     * 13.  (String, ..)：表示至少有一个参数。并且第一个参数类型是String，后面参数类型不限。在参数匹配中，
     * 14.  ..代表任意参数个数和类型
     * 15.  (Object ...)：表示不定个数的参数，且类型都是Object，这里的...不是通配符，而是Java中代表不定参数的意思
     */
    private static final String POINTCUT_VALUE = "call(void android.view.View+.setOnClickListener" +
            "(..))";

    @Pointcut(POINTCUT_VALUE)
    public void methodSetOnClickListener() {

    }

    @Around("methodSetOnClickListener()")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.i(TAG, joinPoint.toShortString());
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getName();
        Log.d(TAG, joinPoint.getKind());
        Log.d(TAG, "methodName: " + methodName);
        // TODO: 2020/6/12 连接点所属的对象，可以获取到相应数据
        Object obj = joinPoint.getTarget();
        if (obj instanceof ViewGroup) {
            Log.i(TAG, "joinPoint's target is ViewGroup");
        } else if (obj instanceof android.view.View) {
            Log.i(TAG, "joinPoint's target is View");
        }
        Object[] args = joinPoint.getArgs();
        Log.d(TAG, Arrays.toString(args));
        //
        joinPoint.proceed();
    }

    @Pointcut("execution(* com.liminghuang..*Activity+.on*(..))")
    public void logForActivity() {}

    @Before("logForActivity()")
    public void log(JoinPoint joinPoint) {
        //对于使用Annotation的AspectJ而言，JoinPoint就不能直接在代码里得到多了，而需要通过
        //参数传递进来。
        Log.d(TAG, joinPoint.toShortString());
    }
}
