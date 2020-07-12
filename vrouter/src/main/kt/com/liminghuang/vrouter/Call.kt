package com.liminghuang.vrouter

import java.io.IOException

/**
 * ProjectName: AOP
 * Description: 路由请求.
 * CreateDate: 2020/7/11 4:44 PM
 * @author: <a href="1569642270@qq.com>colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
interface Call {
    /**
     * 默认需要在异步实现.
     *
     * @return
     * @throws IOException
     */
    fun dispatch(responseCallback: Callback): Unit

    interface Factory {
        fun newCall(request: Request): Call
    }
}