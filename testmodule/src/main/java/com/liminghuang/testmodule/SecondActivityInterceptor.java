package com.liminghuang.testmodule;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.liminghuang.vrouter.Interceptor;
import com.liminghuang.vrouter.Request;
import com.liminghuang.vrouter.Response;

/**
 * ProjectName: AOP
 * Description:
 * CreateDate: 2020/7/11 2:39 PM
 *
 * @author: <a href="1569642270@qq.com>colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class SecondActivityInterceptor implements Interceptor {
    private static final String TAG = "SecondActivityInterceptor";
    private Response actualResponse;

    @Override
    public Response intercept(Chain chain) throws Exception {
        Log.d(TAG, "intercept");
        Request request = chain.request();
        final Request.AddressCompat addressCompat = request.getAddressCompat();
        Context context = request.getAddressCompat().getContext();

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Intercept\n");
        ComponentName componentName = addressCompat.getAddress().getIntent().getComponent();
        String url = componentName != null ? componentName.toShortString() :
                request.getAddressCompat().getAddress().getIntent().getDataString();
        stringBuffer.append("URL: " + url + "\n");

        if (request.getAddressCompat().getContext() instanceof Activity) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert);
            builder.setTitle("Notice");
            builder.setMessage(stringBuffer);
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // chain.cancel();
                }
            });
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    addressCompat.getAddress().getIntent().putExtra("key1", "value3");
                    try {
                        actualResponse = chain.proceed(request);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    // chain.cancel();
                }
            });
            ((Activity) request.getAddressCompat().getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    builder.show();
                }
            });
        }

        // TODO: 2020/7/12 这个response应该设计成一个包装接口，可以获取结果actualResponse
        return new Response() {
            @Override
            public Response behind() {
                return actualResponse;
            }

            @Override
            public boolean isSuccessfully() {
                return behind() != null && behind().isSuccessfully();
            }

            @NonNull
            @Override
            public String toString() {
                return String.format("from %S", TAG);
            }
        };
    }
}
