package com.liminghuang.vrouter

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat

/**
 * ProjectName: AOP
 * Description: 路由调用拦截器.
 * CreateDate: 2020/7/12 3:58 PM
 * @author: <a href="mailto:1569642270@qq.com">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
internal class RouterInvokeInterceptor : Interceptor, ActivityCompat.PermissionCompatDelegate {
    companion object Constants {
        private const val TAG: String = "RouterInvokeInterceptor"
    }

    private var isSucceed: Boolean = false
    private var request: Request? = null

    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        Log.d(TAG, "intercept")
        val originalRequest = chain.request()
        request = originalRequest.next
        val context = originalRequest.addressCompat.context
        val ret: Boolean
        if (context is Activity) {
            ret = try {
                val activity: Activity = context as Activity
                activity.startActivityForResult(originalRequest.addressCompat.address.intent,
                        originalRequest.addressCompat.reqCode,
                        originalRequest.addressCompat.options)
                true
            } catch (e: Exception) {
                false
            }
        } else {
            ret = try {
                context.startActivity(originalRequest.addressCompat.address.intent, originalRequest.addressCompat.options)
                true
            } catch (e: Exception) {
                false
            }
        }
        isSucceed = ret
        return object : Response {
            override fun isSuccessfully(): Boolean = ret
            override fun behind(): Response? = this
        }
    }

    override fun requestPermissions(activity: Activity, permissions: Array<out String>, requestCode: Int): Boolean {
        return false
    }

    override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        request?.let {
            TODO("需要全局的vRouter对象")
        }
//            vRouter.newCall(request).dispatch(object : Callback {
//                override fun onFailure(call: Call, e: Exception) {
//                    Log.e(TAG, String.format("onFailure, %s", e.message))
//                }
//
//                @Throws(Exception::class)
//                override fun onResponse(call: Call, response: Response?) {
//                    Log.d(TAG, String.format("onResponse, %b", response != null && response.isSuccessfully()))
//                }
//            })}
        return false
    }
}