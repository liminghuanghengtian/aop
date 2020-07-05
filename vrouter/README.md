# 路由库API设计
通过编译期注解收集到静态路由表之后，需要考虑如下方面：
1. 路由合规检验
2. 快速匹配路由信息，获得目标页面
3. 快速构建路由链接
4. 支持参数的解析获取，反向构建Intent
5. 支持路由异步拦截、替换处理，采用Interceptor模式
6. 动态降级处理，本地scheme降级成H5的scheme
7. 对目标页面访问控制，例如目标页面需要登录，可在登录后重新打开目标页面
8. Hook
   OnActivityResult，支持RxJava响应式调用，不再需要进行requestCode判断。对确切的startActivityForResult请求如何处理
9. 提供方式：通过服务提供
10. 无对应页面时，可能是三方外部的链接，通过Action：View抛给系统处理
11. 一次路由跳转请求封装成一个request，包括拦截器都可以参考okhttp3

## 设计
![router设计](https://upload-images.jianshu.io/upload_images/53953-ce3ffb119e0d6534.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1200/format/webp)
