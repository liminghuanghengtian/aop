package com.liminghuang.log.plugin

import javassist.CtClass
import javassist.CtMethod
import javassist.bytecode.annotation.Annotation
import org.gradle.api.Project

public class BusInfo {

    //保留当前工程的引用
    Project project
    //当前处理的class
    CtClass clazz
    //带有Bus注解的方法列表
    List<CtMethod> methods = new ArrayList<>()
    //带有Bus注解的注解列表
    List<Annotation> annotations = new ArrayList<>()
    //带有Bus注解的注解id列表
    List<Integer> eventIds = new ArrayList<>()
    //是否是在Activity
    boolean isActivity = false;
    //Activity或Fragment的初始化方法
    CtMethod OnCreateMethod
    //Activity或Fragment的销毁方法
    CtMethod OnDestroyMethod
    //被Register注解标注的初始化方法
    CtMethod BusRegisterMethod
    //被UnRegister注解标注的销毁方法
    CtMethod BusUnRegisterMethod
}