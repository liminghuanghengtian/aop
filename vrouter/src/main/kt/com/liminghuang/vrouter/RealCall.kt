package com.liminghuang.vrouter

import android.util.Log
import okhttp3.internal.platform.Platform
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.atomic.AtomicInteger

/**
 * ProjectName: AOP
 * Description: 路由执行.
 * CreateDate: 2020/7/11 1:37 PM
 *
 * @author: <a href="mailto:1569642270@qq.com">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
class RealCall(val vrouter: VRouter, val originalRequest: Request) : Call {
    // Guarded by this.
    private var executed = false

    companion object {
        private const val TAG: String = "RealCall"
    }

    override fun dispatch(responseCallback: Callback) {
        // 防止并发路由两次
        synchronized(this) {
            check(!executed) { "Already Executed" }
            executed = true
        }

        vrouter.dispatcher.enqueue(AsyncCall(responseCallback, vrouter, originalRequest))
    }

    @Throws(IOException::class)
    private fun getResponseWithInterceptorChain(): Response? {
        // Build a full stack of interceptors.
        val interceptors = mutableListOf<Interceptor>()
        // 登录页拦截器
        vrouter.loginInterceptor?.let { interceptors += it }
        // 其余拦截器
        interceptors += vrouter.interceptors
        // 日志拦截器
        interceptors += vrouter.logInterceptors
        // 路由请求拦截器
        interceptors += RouterInvokeInterceptor()

        val chain = RouteInterceptorChain(
                call = this,
                interceptors = interceptors,
                index = 0,
                request = originalRequest
        )

        try {
            return chain.proceed(originalRequest)
        } catch (e: Exception) {
            throw e
        } finally {
            Log.d(TAG, "getResponseWithInterceptorChain execute finish.")
        }
    }

    internal fun assembleUrl(): String = originalRequest.addressCompat.address.assembleUrl

    /**
     * inner内部类才可以访问声明它的作用域的属性或方法。不加inner就是Nested class，则不行
     */
    internal inner class AsyncCall(private val responseCallback: Callback?,
                                   private val vrouter: VRouter,
                                   private val originalReq: Request) : Runnable {

        // 保证可见性
        @Volatile
        var callsPerHost = AtomicInteger(0)
            private set

        fun reuseCallsPerHostFrom(other: AsyncCall) {
            this.callsPerHost = other.callsPerHost
        }

        val host: String
            get() = originalReq.addressCompat.address.rule.domain

        val request: Request
            get() = originalReq

        val call: RealCall
            get() = this@RealCall

        /**
         * Attempt to enqueue this async call on [executorService]. This will attempt to clean up
         * if the executor has been shut down by reporting the call as failed.
         */
        fun executeOn(executorService: ExecutorService) {
            vrouter.dispatcher.assertThreadDoesntHoldLock()

            var success = false
            try {
                executorService.execute(this)
                success = true
            } catch (e: RejectedExecutionException) {
                val exception = InterruptedException("executor rejected")
                exception.initCause(e)
                responseCallback?.onFailure(this@RealCall, exception)
            } finally {
                if (!success) {
                    vrouter.dispatcher.finished(this) // This call is no longer running!
                }
            }
        }

        override fun run() {
            threadName("VRouter ${assembleUrl()}") {
                var signalledCallback = false
                try {
                    val response = getResponseWithInterceptorChain()
                    signalledCallback = true
                    responseCallback?.onResponse(this@RealCall, response)
                } catch (e: Exception) {
                    if (signalledCallback) {
                        // Do not signal the callback twice!
                        Platform.get().log("Callback failure for ${assembleUrl()}", Platform.INFO, e)
                    } else {
                        responseCallback?.onFailure(this@RealCall, e)
                    }
                } finally {
                    vrouter.dispatcher.finished(this)
                }
            }
        }
    }
}