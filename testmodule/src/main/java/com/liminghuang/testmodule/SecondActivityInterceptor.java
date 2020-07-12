package com.liminghuang.testmodule;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;

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
 * @version: 3.19.0
 * @since: 3.19.0
 */
public class SecondActivityInterceptor implements Interceptor {
    private static final String TAG = "SecondActivityInterceptor";

    @Override
    public Response intercept(Chain call) {
        Request request = call.request();
        final Request.AddressCompat addressCompat = request.getAddressCompat();
        Context context = request.getAddressCompat().getContext();

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Intercept\n");
        ComponentName componentName = addressCompat.getAddress().getIntent().getComponent();
        String url = componentName != null ? componentName.toShortString() :
                request.getAddressCompat().getAddress().getIntent().getDataString();
        stringBuffer.append("URL: " + url + "\n");

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert);
        builder.setTitle("Notice");
        builder.setMessage(stringBuffer);
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // call.cancel();
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addressCompat.getAddress().getIntent().putExtra("key1", "value3");
                try {
                    call.proceed(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // call.cancel();
            }
        });
        builder.show();
        return new Response() {
            @NonNull
            @Override
            public String toString() {
                return String.format("from %S", TAG);
            }
        };
    }
}
