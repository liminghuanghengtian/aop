package com.liminghuang.vrouter

import java.io.IOException

/**
 * ProjectName: AOP
 * Description: 路由拦截器.
 * CreateDate: 2020/7/11 4:35 PM
 * @author: <a href="1569642270@qq.com>colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
interface Interceptor {
    @Throws(IOException::class)
    fun intercept(chain: Chain): Response?

    companion object {
        /**
         * Constructs an interceptor for a lambda. This compact syntax is most useful for inline
         * interceptors.
         *
         * ```
         * val interceptor = Interceptor { chain: Interceptor.Chain ->
         *     chain.proceed(chain.request())
         * }
         * ```
         */
        inline operator fun invoke(crossinline block: (chain: Chain) -> Response): Interceptor =
                object : Interceptor {
                    override fun intercept(chain: Chain) = block(chain)
                }
    }

    interface Chain {
        /** Returns the original request that initiated this call.  */
        fun request(): Request

        @Throws(Exception::class)
        fun proceed(request: Request): Response?

        fun call(): Call
    }
}