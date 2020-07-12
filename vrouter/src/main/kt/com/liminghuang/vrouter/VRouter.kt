package com.liminghuang.vrouter

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.liminghuang.route.Constants
import com.liminghuang.route.RouteRule
import com.liminghuang.vrouter.match.impl.KeyMatcher
import com.liminghuang.vrouter.match.impl.RouterMatcherImpl
import com.liminghuang.vrouter.match.impl.UrlMatcher
import com.liminghuang.vrouter.toImmutableList
import com.liminghuang.vrouter.immutableListOf

/**
 * ProjectName: AOP
 * Description:
 * CreateDate: 2020/7/11 9:13 PM
 * @author: <a href="mailto:liming.huang@tuya.com">colin</a>
 * @version: 3.19.0
 * @since: 3.19.0
 */
open class VRouter internal constructor(builder: Builder) : Call.Factory {

    init {
        Log.d(TAG, "VRouter init.")
    }

    @get:JvmName("dispatcher")
    val dispatcher: Dispatcher = builder.dispatcher

    @get:JvmName("loginInterceptor")
    val loginInterceptor: Interceptor? = builder.loginInterceptor

    /**
     * Returns an immutable list of interceptors that observe the full span of each call: from before
     * the connection is established (if any) until after the response source is selected (either the
     * origin server, cache, or both).
     */
    @get:JvmName("interceptors")
    val interceptors: List<Interceptor> = builder.interceptors.toImmutableList()

    /**
     * Returns an immutable list of interceptors that observe a single network request and response.
     * These interceptors must call [Interceptor.Chain.proceed] exactly once: it is an error for
     * a network interceptor to short-circuit or repeat a network request.
     */
    @get:JvmName("networkInterceptors")
    val logInterceptors: List<Interceptor> = builder.logInterceptors.toImmutableList()

    constructor() : this(Builder())

    override fun newCall(request: Request): Call = RealCall(this, request)

    open fun newBuilder(): Builder = Builder(this)

    @JvmName("-deprecated_dispatcher")
    @Deprecated(
            message = "moved to val",
            replaceWith = ReplaceWith(expression = "dispatcher"),
            level = DeprecationLevel.ERROR)
    fun dispatcher(): Dispatcher = dispatcher

    @JvmName("-deprecated_interceptors")
    @Deprecated(
            message = "moved to val",
            replaceWith = ReplaceWith(expression = "interceptors"),
            level = DeprecationLevel.ERROR)
    fun interceptors(): List<Interceptor> = interceptors

    @JvmName("-deprecated_networkInterceptors")
    @Deprecated(
            message = "moved to val",
            replaceWith = ReplaceWith(expression = "logInterceptors"),
            level = DeprecationLevel.ERROR)
    fun logInterceptors(): List<Interceptor> = logInterceptors

    class Builder constructor() {
        // TODO: 2020/7/11 定义全局的配置，例如：登录页信息
        internal var dispatcher: Dispatcher = Dispatcher()
        internal val interceptors: MutableList<Interceptor> = mutableListOf()
        internal val logInterceptors: MutableList<Interceptor> = mutableListOf()
        internal var loginInterceptor: Interceptor? = null

        internal constructor(vrouter: VRouter) : this() {
            this.dispatcher = vrouter.dispatcher
            this.loginInterceptor = vrouter.loginInterceptor
            this.interceptors += vrouter.interceptors
            this.logInterceptors += vrouter.logInterceptors
        }

        fun loginInterceptor(loginInterceptor: Interceptor) = apply { this.loginInterceptor = loginInterceptor }

        /**
         * Sets the dispatcher used to set policy and execute asynchronous requests. Must not be null.
         */
        fun dispatcher(dispatcher: Dispatcher) = apply {
            this.dispatcher = dispatcher
        }

        /**
         * Returns a modifiable list of interceptors that observe the full span of each call: from
         * before the connection is established (if any) until after the response source is selected
         * (either the origin server, cache, or both).
         */
        fun interceptors(): MutableList<Interceptor> = interceptors

        fun addInterceptor(interceptor: Interceptor) = apply {
            interceptors += interceptor
        }

        @JvmName("-addInterceptor") // Prefix with '-' to prevent ambiguous overloads from Java.
        inline fun addInterceptor(crossinline block: (chain: Interceptor.Chain) -> Response) =
                addInterceptor(Interceptor { chain -> block(chain) })

        /**
         * Returns a modifiable list of interceptors that observe a single network request and response.
         * These interceptors must call [Interceptor.Chain.proceed] exactly once: it is an error for a
         * network interceptor to short-circuit or repeat a network request.
         */
        fun logInterceptors(): MutableList<Interceptor> = logInterceptors

        fun addLogInterceptor(interceptor: Interceptor) = apply {
            logInterceptors += interceptor
        }

        @JvmName("-addLogInterceptor") // Prefix with '-' to prevent ambiguous overloads from Java.
        inline fun addLogInterceptor(crossinline block: (chain: Interceptor.Chain) -> Response) =
                addLogInterceptor(Interceptor { chain -> block(chain) })

        fun build(): VRouter = VRouter(this)
    }

    companion object {
        private const val TAG = "VRouter"
        internal val DEFAULT_PROTOCOLS = immutableListOf(Constants.NATIVE_SCHEME)
        private val sKeyMatcher = KeyMatcher(RouterMatcherImpl())
        private val sUrlMatcher = UrlMatcher(RouterMatcherImpl())

        fun buildAddress(context: Context, tag: String, params: Bundle?): Address? {
            val rule = sKeyMatcher.match(tag)
            var intent: Intent?
            return rule?.let {
                intent = Intent()
                if (RouteRule.Mode.NATIVE == it.mode) {
                    // 明确的组件名称，就不需要系统去匹配intent-filter
                    intent!!.component = ComponentName(context.packageName, it.qualified)
                    params?.let {
                        intent!!.putExtras(params)
                    }
                } else if (RouteRule.Mode.H5 == it.mode) {
                    // TODO: 2020/7/11
                    Log.w(TAG, "need thinking")
                }
                return Address(it, intent)
            }
        }

        fun buildAddress(context: Context, url: String?): Address? {
            val rule = sUrlMatcher.match(url)
            var intent: Intent?
            return rule?.let {
                intent = Intent()
                if (RouteRule.Mode.NATIVE == it.mode) {
                    // 明确的组件名称，就不需要系统去匹配intent-filter
                    intent!!.component = ComponentName(context.packageName, it.qualified)
                    val uri = Uri.parse(url)
                    val paramNames = uri.queryParameterNames
                    for (paramName in paramNames) {
                        intent!!.putExtra(paramName, uri.getQueryParameter(paramName))
                    }
                } else if (RouteRule.Mode.H5 == it.mode) {
                    intent = Intent(Intent.ACTION_VIEW)
                    intent!!.addCategory(Intent.CATEGORY_BROWSABLE)
                    intent!!.addCategory(Intent.CATEGORY_DEFAULT)
                    val uri = Uri.parse(url)
                    intent!!.data = uri
                }
                return Address(it, intent)
            }
        }
    }
}