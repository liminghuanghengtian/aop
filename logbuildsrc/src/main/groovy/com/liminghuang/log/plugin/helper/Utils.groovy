package com.liminghuang.log.plugin.helper

import javassist.CtMethod
import proguard.classfile.ClassPool

public class Utils {
    static String BusErr = "大哥注意哦，非Activity和Fragment中使用@BusRegister必须和@BusUnRegister一起使用，才能自动生成注册和反注册代码"

    /**
     * 事先载入相关类
     * @param pool
     */
    static void importBaseClass(ClassPool pool) {
        pool.importPackage(LogTimeHelper.LogTimeAnnotation);
        pool.importPackage(BusHelper.OkBusAnnotation);
        pool.importPackage(BusHelper.OkBusRegisterAnnotation);
        pool.importPackage(BusHelper.OkBusUnRegisterAnnotation);
        pool.importPackage("android.os.Bundle");
        pool.importPackage("com.base.event.OkBus")
        pool.importPackage("com.base.event.Event")
        pool.importPackage("android.os.Message")
    }

    static String getSimpleName(CtMethod ctmethod) {
        def methodName = ctmethod.getName();
        return methodName.substring(
                methodName.lastIndexOf('.') + 1, methodName.length());
    }

    static String getClassName(int index, String filePath) {
        // .class = 6
        int end = filePath.length() - 6
        // 文件路径分隔符合替换成包名分隔点符号
        return filePath.substring(index, end).replace('\\', '.').replace('/', '.')
    }
}