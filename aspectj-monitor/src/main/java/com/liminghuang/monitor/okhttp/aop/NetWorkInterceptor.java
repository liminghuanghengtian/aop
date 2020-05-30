package com.liminghuang.monitor.okhttp.aop;


import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RealInterceptorChain;
import okio.Buffer;
import okio.BufferedSource;

public class NetWorkInterceptor implements Interceptor {
    private static final String TAG = "NetWorkInterceptor";

    public NetWorkInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response;
        RealInterceptorChain realInterceptorChain = (RealInterceptorChain) chain;
        HttpEventListener httpEventListener = (HttpEventListener) realInterceptorChain.eventListener();
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "HTTP FAILED: " + e);
            recordError(request, e.getMessage(), httpEventListener);
            throw e;
        }
        recordInfo(request, response, httpEventListener);
        Log.d(TAG, "okhttp chain.proceed end.");
        return response;
    }

    private void recordError(Request request, String message, HttpEventListener httpEventListener) {
        try {
            // TODO: 2020/5/24 持久化
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    ///////////////////////////////////////////////////////////////////////////
    // 记录信息
    ///////////////////////////////////////////////////////////////////////////
    private void recordInfo(Request request, Response response, HttpEventListener httpEventListener) {
        try {
            ResponseBody responseBody = response.body();
            long contentLength = responseBody.contentLength();
            BufferedSource source = responseBody.source();
            try {
                source.request(Long.MAX_VALUE); // Buffer the entire body.
            } catch (IOException e) {
                e.printStackTrace();
            }
            Buffer buffer = source.buffer();
            Charset charset = Charset.forName("utf-8");
            String body = null;
            if (contentLength != 0) {
                body = buffer.clone().readString(charset);
            }

            // TODO: 2020/5/24 持久化


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
