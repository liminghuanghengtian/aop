package com.liminghuang.route.processor;

import com.liminghuang.route.annotation.RouteModule;
import com.liminghuang.route.annotation.RouteTarget;
import com.liminghuang.route.gen.ModuleCompositionGenerator;
import com.liminghuang.route.gen.ModuleProxyGenerator;
import com.liminghuang.route.gen.RouteTableGenerator;
import com.liminghuang.route.model.RouteModuleAnnotatedClass;
import com.liminghuang.route.model.RouteTargetAnnotatedClass;
import com.liminghuang.route.util.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;

/**
 * Description: 路由处理器.
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class RouterProcessor implements IProcess {
    private static final String TAG = "RouterProcessor";
    private Map<String, RouteModuleAnnotatedClass> mModuleAnnotatedClassMap = new HashMap<>();
    private Map<String, RouteTargetAnnotatedClass> mTargetAnnotatedClassMap = new HashMap<>();
    Map<RouteModuleAnnotatedClass, List<RouteTargetAnnotatedClass>> maps = null;

    private final String outputPath;
    private final boolean isMain;

    RouterProcessor(String outputPath, boolean isMain) {
        this.outputPath = outputPath;
        this.isMain = isMain;
    }

    @Override
    public boolean process(RoundEnvironment roundEnv, Filer filer, Elements elementUtils,
                           Messager messager) {
        messager.printMessage(Kind.OTHER, String.format("%s is processing...", TAG));
        // process() will be called several times
        mModuleAnnotatedClassMap.clear();
        mTargetAnnotatedClassMap.clear();

        RouteModuleAnnotatedClass routeModuleAnnotatedClass;
        try {
            routeModuleAnnotatedClass = processRouteModule(roundEnv, messager);
            assert routeModuleAnnotatedClass != null;

            processRouteTarget(roundEnv, routeModuleAnnotatedClass, elementUtils, messager);
        } catch (Exception e) {
            messager.printMessage(Kind.ERROR, e.getMessage());
            // stop process
            return true;
        }

        if (maps != null && maps.size() > 0) {
            generateCodes(maps, filer, elementUtils, messager);
        }
        return true;
    }

    private RouteModuleAnnotatedClass processRouteModule(RoundEnvironment roundEnv, Messager messager) throws IllegalArgumentException {
        // 获得被该注解声明的元素
        Set<? extends Element> moduleElements = roundEnv.getElementsAnnotatedWith(RouteModule.class);
        if (moduleElements.size() > 0) {
            // 存放模块及内部路由
            maps = new HashMap<RouteModuleAnnotatedClass, List<RouteTargetAnnotatedClass>>(moduleElements.size() / 3 * 4);
            for (Element moduleE : moduleElements) {
                // 校验元素类型
                if (moduleE.getKind() == ElementKind.CLASS) {
                    TypeElement classElement = (TypeElement) moduleE;
                    return getRouteModuleAnnotatedClass(classElement, messager);
                } else {
                    String errorMsg = String.format("Only class can be annotated with @%s", RouteModule.class.getSimpleName());
                    messager.printMessage(Kind.ERROR, errorMsg);
                    throw new IllegalArgumentException(errorMsg);
                }
            }
        }

        return null;
    }

    private void processRouteTarget(RoundEnvironment roundEnv, RouteModuleAnnotatedClass routeModuleAnnotatedClass,
                                    Elements elementUtils, Messager messager) {
        Set<? extends Element> targetElements = roundEnv.getElementsAnnotatedWith(RouteTarget.class);

        List<RouteTargetAnnotatedClass> routeTargetAnnClzList = null;
        for (Element targetE : targetElements) {
            assert maps != null;

            // 校验元素类型
            if (targetE.getKind() == ElementKind.CLASS) {
                TypeElement classElement = (TypeElement) targetE;
                RouteTargetAnnotatedClass routeTargetAnnotatedClass =
                        getRouteTargetAnnotatedClass(classElement,
                                routeModuleAnnotatedClass.getModuleInfo().getDomain(),
                                elementUtils,
                                messager);

                if (routeTargetAnnClzList == null) {
                    routeTargetAnnClzList = new ArrayList<RouteTargetAnnotatedClass>();
                }
                if (!maps.containsKey(routeModuleAnnotatedClass)) {
                    maps.put(routeModuleAnnotatedClass, routeTargetAnnClzList);
                }
                routeTargetAnnClzList.add(routeTargetAnnotatedClass);
            } else {
                String errorMsg = String.format("Only class can be annotated with @%s", RouteTarget.class.getSimpleName());
                messager.printMessage(Kind.ERROR, errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
        }
    }

    private RouteModuleAnnotatedClass getRouteModuleAnnotatedClass(TypeElement element,
                                                                   Messager messager) {
        String fullClassName = element.getQualifiedName().toString();
        RouteModuleAnnotatedClass annotatedClass = mModuleAnnotatedClassMap.get(fullClassName);
        if (annotatedClass == null) {
            annotatedClass = new RouteModuleAnnotatedClass(element, messager);
            mModuleAnnotatedClassMap.put(fullClassName, annotatedClass);
        }
        return annotatedClass;
    }

    private RouteTargetAnnotatedClass getRouteTargetAnnotatedClass(TypeElement element,
                                                                   String domain,
                                                                   Elements elementUtils,
                                                                   Messager messager) {
        String fullClassName = element.getQualifiedName().toString();
        RouteTargetAnnotatedClass annotatedClass = mTargetAnnotatedClassMap.get(fullClassName);
        if (annotatedClass == null) {
            annotatedClass = new RouteTargetAnnotatedClass(element, domain, elementUtils, messager);
            mTargetAnnotatedClassMap.put(fullClassName, annotatedClass);
        }
        return annotatedClass;
    }


    private void generateCodes(Map<RouteModuleAnnotatedClass, List<RouteTargetAnnotatedClass>> maps, Filer filer, Elements elementUtils, Messager messager) {
        print(maps, messager);
        // 遍历maps
        for (RouteModuleAnnotatedClass key : maps.keySet()) {
            try {
                messager.printMessage(Kind.NOTE, String.format("Generating file for module{%s}",
                        key.getClassElement().getSimpleName()));
                new RouteTableGenerator(key, maps.get(key), elementUtils, messager).generate().writeTo(filer);
                new ModuleProxyGenerator(key, maps.get(key), elementUtils, messager).generate().writeTo(filer);
            } catch (IOException e) {
                messager.printMessage(Kind.ERROR, String.format("Generate file failed, reason: %s",
                        e.getMessage()));
            }
        }
        if (isMain) {
            // try {
            //     messager.printMessage(Kind.ERROR, "Generating file for main app module");
            //     // new ModuleCompositionGenerator(elementUtils, messager).generate().writeTo(filer);
            // } catch (IOException e) {
            //     e.printStackTrace();
            // }
        }
    }

    private void print(Map<RouteModuleAnnotatedClass, List<RouteTargetAnnotatedClass>> maps, Messager messager) {
        if (outputPath == null || outputPath.length() == 0) {
            return;
        }

        File dir = new File(outputPath);
        if (!dir.exists() && dir.mkdirs()) {
            messager.printMessage(Kind.NOTE, String.format("%s 创建成功", dir.getAbsolutePath()));
        }
        messager.printMessage(Kind.NOTE, String.format("print route table to %s",
                dir.getAbsolutePath()));

        // 遍历map
        for (RouteModuleAnnotatedClass key : maps.keySet()) {
            // 创建文件
            File file = new File(dir,
                    key.getModuleInfo().getQualified().replaceAll("\\.", "_") + ".json");
            if (file.exists()) {
                FileUtils.deleteAllFilesOfDir(file);
            }
            try {
                /**
                 * 编写json文件内容
                 */
                FileWriter fw = new FileWriter(file);
                fw.append("{\n")
                        .append("   ")
                        .append("\"").append("class").append("\"").append(":")
                        .append("\"").append(key.getModuleInfo().getSimpleName()).append("\"")
                        .append(",\n");
                fw.append("   ")
                        .append("\"").append("targets").append("\"").append(":").append(" {\n");
                List<RouteTargetAnnotatedClass> targetList = maps.get(key);

                for (int i = 0; i < targetList.size(); i++) {
                    RouteTargetAnnotatedClass target = targetList.get(i);
                    fw.append("      ")
                            .append("\"").append(target.getRuleInfo().getKey()).append("\"").append(":")
                            .append("\"").append(target.getRuleInfo().getQualified()).append("\"");
                    if (i < targetList.size() - 1) {
                        fw.append(",");
                        fw.append("\n");
                    }
                }
                fw.append("\n").append("   }\n").append("}");
                fw.flush();
                fw.close();
            } catch (IOException e) {
                messager.printMessage(Kind.ERROR, e.getMessage());
            }
        }
    }
}
