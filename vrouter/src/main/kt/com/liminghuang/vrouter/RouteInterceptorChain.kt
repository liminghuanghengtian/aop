package com.liminghuang.vrouter


/**
 * ProjectName: AOP
 * Description:
 * CreateDate: 2020/7/12 4:02 PM
 * @author: <a href="mailto:1569642270@qq.com">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
class RouteInterceptorChain(
        internal val call: RealCall,
        private val interceptors: List<Interceptor>,
        private val index: Int,
        internal val request: Request
) : Interceptor.Chain {

    private var calls: Int = 0

    internal fun copy(
            index: Int = this.index,
            request: Request = this.request
    ) = RouteInterceptorChain(call, interceptors, index, request)

    override fun call(): Call = call

    override fun request(): Request = request

    @Throws(Exception::class)
    override fun proceed(request: Request): Response? {
        check(index < interceptors.size)

        calls++

        check(calls == 1) {
            "interceptor ${interceptors[index - 1]} must call proceed() exactly once"
        }


        // Call the next interceptor in the chain.
        val next = copy(index = index + 1, request = request)
        val interceptor = interceptors[index]

        // 结果可空
        @Suppress("USELESS_ELVIS")
        val response = interceptor.intercept(next)

        check(index + 1 >= interceptors.size || next.calls == 1) {
            "interceptor $interceptor must call proceed() exactly once"
        }


        check(response == null || !response.isSuccessfully()) {
            "interceptor $interceptor returned a response is failed!"
        }

        return response
    }
}