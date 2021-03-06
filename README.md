# Project结构
- app 上层集成模块
- testmodule 测试模块
- aspectjcache 基于Aspectj实现的注解缓存
- aspectjmonitor 基于Aspectj实现的okHttp网络请求过程监控
- buildsrc Aspectj编译插件
- logbuildsrc javassist字节码处理
- Repository 本地仓库
- route-compiler 路由注解处理器
- route-lib 路由基础库
- route-injector 路由参数自动注入
- injector-compiler 参数自动注入注解处理器
- viewfinder 视图注入
- viewfinder-annotation 视图注入注解
- viewfinder-compiler 视图注入注解处理器

# 待办
路由库API设计
路由增加拦截和路由结果判断

# 路由收集设计
## APT(Annotation Processing Tool)/Pluggable Annotation Processing
APT是一种处理注释的工具，能够对源代码文件检测并找出其中的注解。我们通过定义编译期的注解，继承AbstractProcessor实现生成代码逻辑，借以生成的中间代码来完成收集路由表的功能。Android Studio 3.0之后android-apt切换成官方annotationProcessor，主要是因为AGP3.0.0版本升级行为变更导致。
> 附：[Android Gradle 插件版本说明](https://developer.android.google.cn/studio/releases/gradle-plugin.html)

## 核心设计
- 两个注解：@RouteTarget、@RouteModule
- 四个抽象：IRouteCollector、IRouteModule、AbsRouteModule、IModuleGenerator
- 中间代码生成器：RouteTableGenerator、ModuleProxyGenerator、ModuleCompositionGenerator

![抽象设计](./doc/route_abstract.jpeg)

### 设计思想
1. 收集各个模块的路由信息，形成模块内的路由表
2. 组合各模块的路由表，形成总路由表
3. 各模块可定制额外的路由规则，例如：加入H5的路由协议
4. 路由参数由切面处理，生成切面注入器，自动注入参数，例如app模块的MainActivity$$Injector.class

### 思路
1. 依据url路径规范 @see RuleInfo（注解信息的存储）
2. 通过编译选项isMain来指定主模块，各模块路由信息通过编译选项buildPath统一输出到指定路径下，例如此处为project的build/router目录下。以此来实现主模块的路由组合功能

### 生成什么样的中间代码？
1. RouteTableGenerator生成：RouteCollector_{domain}.java
2. ModuleProxyGenerator生成：{ModuleClzSimpleName}Decorator.java
3. ModuleCompositionGenerator生成：ModuleComposition.java，这个类只在最上层的模块生成，例如：app模块

### Element
java类的结构类比前端的dom结构，每个元素有相应的节点类型，分别代表了包、类、方法等等。
```java
package com.example; // PackageElement

public class Foo { // TypeElement

    private int a; // VariableElement
    private Foo other; // VariableElement

    public Foo() {} // ExecutableElement

    // ExecutableElement
    public void setA( 
        int newA // VariableElement
    ) {}
}
```
