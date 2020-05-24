package com.liminghuang.monitor.okhttp.aop;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

import androidx.annotation.Nullable;
import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

public class HttpEventListener extends EventListener {
    private static final String TAG = "HttpEventListener";
    private String mUrl;
    public long mCallStartTime;
    public long mDnsStartTime;
    public long mConnectStartTime;
    public long mSecureConnectStartTime;
    public long mConnectionAcquiredTime;
    public long mRequestHeadersStartTime;
    public long mRequestBodyStartTime;
    public long mResponseHeadersStartTime;
    public long mResponseBodyStartTime;

    public long callTime;
    public long callEndTime;
    public long callFailedTime;
    public long dnsTime;
    public long sslConnectTime;
    public long tcpConnectTime;
    public long reqHeaderTime;
    public long reqBodyTime;
    public long resHeaderTime;
    public long resBodyTime;


    public static final Factory FACTORY = new Factory() {

        @Override
        public EventListener create(Call call) {
            return new HttpEventListener(call);
        }
    };

    public HttpEventListener(Call call) {

    }


    private void recordLog(String name, long time) {
        String log = mUrl + "---" + name + "---" + time;
        Log.i(TAG, log);
    }

    public long getConnectTime() {
        if (tcpConnectTime == 0) {
            return sslConnectTime;
        }
        return tcpConnectTime;
    }

    public long getQueueTime() {
        return mConnectionAcquiredTime - mCallStartTime - getConnectTime() - dnsTime;
    }

    public long getTransmitTime() {
        return reqHeaderTime + reqBodyTime + resHeaderTime + resBodyTime;
    }

    public long getCallTime() {
        return callTime;
    }


    public String getEveryTimesInfo() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("callTime", getCallTime());  //整个网络请求过程的耗时 total
            jsonObject.put("dnsTime", dnsTime);  //DNS解析耗时
            jsonObject.put("queueTime", getQueueTime());//排队时间
            jsonObject.put("connectTime", getConnectTime());//包含tcp和ssl连接
            jsonObject.put("transmitTime", getTransmitTime());//包含request，response时间
            jsonObject.put("callFailedTime", callFailedTime); //callFailedTime  只不过请求失败了
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    @Override
    public void callStart(Call call) {
        mCallStartTime = System.currentTimeMillis();
        mUrl = call.request().url().toString();
        Log.i(TAG, mUrl + "---callStart---");
        Log.i(TAG, "callStart---" + mUrl + this.hashCode());

    }

    @Override
    public void dnsStart(Call call, String domainName) {
        mDnsStartTime = System.currentTimeMillis();
        Log.i(TAG, mUrl + "---dnsStart---");
    }

    @Override
    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
        dnsTime += System.currentTimeMillis() - mDnsStartTime;
    }

    @Override
    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        mConnectStartTime = System.currentTimeMillis();
        Log.i(TAG, mUrl + "---connectStart---");
    }

    @Override
    public void secureConnectStart(Call call) {
        mSecureConnectStartTime = System.currentTimeMillis();
        Log.i(TAG, mUrl + "---secureConnectStart---");
    }

    @Override
    public void secureConnectEnd(Call call, @Nullable Handshake handshake) {
        sslConnectTime += System.currentTimeMillis() - mSecureConnectStartTime;
        recordLog("sslConnectTime", sslConnectTime);
    }

    @Override
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy,
                           @Nullable Protocol protocol) {
        tcpConnectTime += System.currentTimeMillis() - mConnectStartTime;
        recordLog("tcpConnectTime", tcpConnectTime);
    }

    @Override
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy,
                              @Nullable Protocol protocol, IOException ioe) {
        long connectFailedTime = System.currentTimeMillis() - mConnectStartTime;
        recordLog("connectFailed", connectFailedTime);
    }

    @Override
    public void connectionAcquired(Call call, Connection connection) {
        mConnectionAcquiredTime = System.currentTimeMillis();
    }

    @Override
    public void connectionReleased(Call call, Connection connection) {
    }

    @Override
    public void requestHeadersStart(Call call) {
        mRequestHeadersStartTime = System.currentTimeMillis();
        Log.i(TAG, mUrl + "---requestHeadersStart---");
    }

    @Override
    public void requestHeadersEnd(Call call, Request request) {
        reqHeaderTime += System.currentTimeMillis() - mRequestHeadersStartTime;
        recordLog("reqHeaderTime", reqHeaderTime);
    }

    @Override
    public void requestBodyStart(Call call) {
        mRequestBodyStartTime = System.currentTimeMillis();
        Log.i(TAG, mUrl + "---requestBodyStart---");
    }

    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        reqBodyTime += System.currentTimeMillis() - mRequestBodyStartTime;
        recordLog("reqBodyTime", reqBodyTime);
    }

    @Override
    public void responseHeadersStart(Call call) {
        mResponseHeadersStartTime = System.currentTimeMillis();
        Log.i(TAG, mUrl + "---responseHeadersStart---");
    }

    @Override
    public void responseHeadersEnd(Call call, Response response) {
        resHeaderTime += System.currentTimeMillis() - mResponseHeadersStartTime;
        recordLog("resHeaderTime", resHeaderTime);
    }

    @Override
    public void responseBodyStart(Call call) {
        mResponseBodyStartTime = System.currentTimeMillis();
        Log.i(TAG, mUrl + "---responseBodyStart---");
    }


    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        resBodyTime += System.currentTimeMillis() - mResponseBodyStartTime;
        recordLog("resBodyTime", resBodyTime);
    }

    @Override
    public void callEnd(Call call) {
        callEndTime = System.currentTimeMillis();
        callTime = System.currentTimeMillis() - mCallStartTime;
        Log.i(TAG, "callEnd---" + mUrl + this.hashCode());
    }

    @Override
    public void callFailed(Call call, IOException ioe) {
        callFailedTime = System.currentTimeMillis() - mCallStartTime;
        recordLog("callFailed", callFailedTime);
    }
}