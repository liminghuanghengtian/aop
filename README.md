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
- 中间代码生成：RouteTableGenerator、ModuleProxyGenerator、ModuleCompositionGenerator
### 设计思想
1. 收集各个模块的路由信息，形成模块内的路由表
2. 组合各模块的路由表，形成总路由表
3. 各模块可定制额外的路由规则
### 设计模式
1. 装饰模式（门面模式）
2. 构建者模式
