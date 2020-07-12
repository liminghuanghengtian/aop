package com.liminghuang.vrouter

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat
import java.lang.Exception

/**
 * ProjectName: AOP
 * Description:
 * CreateDate: 2020/7/12 3:58 PM
 * @author: <a href="mailto:1569642270@qq.com">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
internal class RouterInvokeInterceptor : Interceptor, ActivityCompat.PermissionCompatDelegate {
    companion object {
        private const val TAG: String = "RouterInvokeInterceptor"
    }

    private var isSucceed: Boolean = false

    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        Log.d(TAG, "intercept")
        val request = chain.request()
        val context = request.addressCompat.context
        val ret: Boolean
        if (context is Activity) {
            ret = try {
                val activity: Activity = context as Activity
                activity.startActivityForResult(request.addressCompat.address.intent,
                        request.addressCompat.reqCode,
                        request.addressCompat.options)
                true
            } catch (e: Exception) {
                false
            }
        } else {
            ret = try {
                context.startActivity(request.addressCompat.address.intent, request.addressCompat.options)
                true
            } catch (e: Exception) {
                false
            }
        }
        return object : Response {
            override fun isSuccessfully(): Boolean = ret
        }
    }

    override fun requestPermissions(activity: Activity, permissions: Array<out String>, requestCode: Int): Boolean {
        return false
    }

    override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return false
    }
}