package com.liminghuang.log.plugin

import com.liminghuang.log.plugin.helper.*
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.bytecode.DuplicateMemberException
import javassist.bytecode.annotation.Annotation
import org.gradle.api.Project

public class MyInject {
    /**
     * ClassPool 对象是表示类文件的 CtClass 对象的容器。它根据需要读取类文件以构造 CtClass 对象, 并记录构造对象以响应以后的访问
     * getDefault 返回的 ClassPool 对象搜索默认的系统搜索路径。
     */
    private final static ClassPool pool = ClassPool.getDefault()

    /**
     *
     * @param path 一级总路径
     * @param packageName 应用内包名，com
     * @param project
     */
    public static void injectDir(String path, String packageName, Project project) {
        project.logger.error(" inject path -> " + path)
        // 添加类搜索路径
        pool.appendClassPath(path)
        // project.android.bootClasspath 加入android.jar，否则找不到android相关的所有类
        pool.appendClassPath(project.android.bootClasspath[0].toString())
        // 导入基础类路径
        Utils.importBaseClass(pool)

        File dir = new File(path)
        if (dir.isDirectory()) {
            dir.eachFileRecurse { File file ->
                String filePath = file.absolutePath

                // 确保当前文件是class文件，并且不是系统自动生成的class文件
                if (filePath.endsWith(".class") && !filePath.contains('R$') && !filePath.contains('$')//代理类
                        && !filePath.contains('R.class') && !filePath.contains("BuildConfig.class")) {
                    // 判断当前目录是否属于指定的包名里面
                    int index = filePath.indexOf(packageName);
                    boolean isMyPackage = index != -1;
                    if (isMyPackage) {
                        String className = Utils.getClassName(index, filePath)
                        // CtClass（编译时类）是类文件的抽象表示形式，是处理类文件的句柄
                        CtClass c = pool.getCtClass(className)
                        // CtClass对象由 writeFile ()、toClass () 或 toBytecode () 转换为类文件, Javassist 将冻结该 CtClass 对象。
                        // toClass() 请求当前线程的上下文类加载程序加载由 CtClass 表示的类文件。它返回一个表示已加载类的 java.lang.Class 对象。
                        if (c.isFrozen()) {
                            // 冻结类解冻
                            c.defrost()
                        }

                        BusInfo mBusInfo = new BusInfo()
                        mBusInfo.setProject(project)
                        mBusInfo.setClazz(c)
                        // 是否Activity标记
                        if (c.getName().endsWith("Activity") || c.getSuperclass().getName().endsWith("Activity")) {
                            mBusInfo.setIsActivity(true)
                        }

                        boolean isAnnotationByBus = false;
                        // 遍历所有方法：getDeclaredMethods获取自己申明的方法，c.getMethods()会把所有父类的方法都加上
                        for (CtMethod ctMethod : c.getDeclaredMethods()) {
                            String methodName = Utils.getSimpleName(ctMethod);
                            // onCreate方法查找
                            if (BusHelper.ON_CREATE.contains(methodName)) {
                                mBusInfo.setOnCreateMethod(ctMethod)
                            }
                            // onDestroy方法查找
                            if (BusHelper.ON_DESTROY.contains(methodName)) {
                                mBusInfo.setOnDestroyMethod(ctMethod)
                            }

                            // 注解处理
                            for (Annotation mAnnotation : ctMethod.getAnnotations()) {
                                if (mAnnotation.annotationType().canonicalName.equals(BusHelper.OkBusRegisterAnnotation))
                                    mBusInfo.setBusRegisterMethod(ctMethod)

                                if (mAnnotation.annotationType().canonicalName.equals(BusHelper.OkBusUnRegisterAnnotation))
                                    mBusInfo.setBusUnRegisterMethod(ctMethod)

                                if (mAnnotation.annotationType().canonicalName.equals(BusHelper.OkBusAnnotation)) {
                                    project.logger.info " method:" + c.getName() + " -" + ctMethod.getName()
                                    mBusInfo.methods.add(ctMethod)
                                    mBusInfo.annotations.add(mAnnotation)
                                    if (!isAnnotationByBus) {
                                        isAnnotationByBus = true
                                    }
                                }
                            }
                        }

                        if ((mBusInfo.BusRegisterMethod != null && mBusInfo.BusUnRegisterMethod == null)
                                || (mBusInfo.BusRegisterMethod == null && mBusInfo.BusUnRegisterMethod != null))
                            assert false: Utils.getBusErr()

                        if (mBusInfo != null && isAnnotationByBus) {
                            try {
                                BusHelper.intBus(mBusInfo, path)
                            } catch (DuplicateMemberException e) {

                            }
                        }

                        // 用完一定记得要卸载，否则pool里的永远是旧的代码
                        c.detach()
                    }else{
                        project.logger.error(" filePath: ${filePath} is not my package")
                    }
                }
            }
        } else {
            project.logger.error(" is file: ${path}")
        }
    }
}