package com.liminghuang.demo;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.liminghuang.javassist.lib.annotaion.Bus;
import com.liminghuang.route.annotation.RouteTarget;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * 所有以on开头的方法都会被注入打印方法的代码.
 */
@RouteTarget(target = "/second", tag = "second_page", needLogin = false)
public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "SecondActivity";
    private static final int REQ_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        TextView tv = findViewById(R.id.tv_content);
        // fixme 以下的方法调用会被切面替换掉执行逻辑
        tv.setOnClickListener(new MyOnClickListener(this));
    }

    private void doLocation() {
        Log.i(TAG, "模拟定位中...");
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "已授权", Toast.LENGTH_LONG).show();
                doLocation();
            } else {
                // 是否拒绝过
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Log.d(TAG, "shouldShowRequestPermissionRationale：用户曾拒绝定位权限，并选择了不再提示");
                    // 弹窗或者页面告知用户原理，并提供交互跳转到系统的应用设置页面
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    AlertDialog dialog = builder.setTitle("权限说明")
                            .setMessage("请前往设置->应用-><应用名称>->权限中打开相关权限，否则功能无法正常运行！")
                            .setPositiveButton("好的，去授权", new Dialog.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    try {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("依旧拒绝", new Dialog.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d(TAG, "拒绝");
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    dialog.show();
                } else {
                    Log.d(TAG, "shouldShowRequestPermissionRationale：用户此前拒绝了权限申请，请求定位权限");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_CODE);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG,
                "onRequestPermissionsResult -> reqCode:" + requestCode + ", permissions: " + Arrays.toString(permissions) +
                        ", grantResults: " + Arrays.toString(grantResults));
        if (requestCode == REQ_CODE && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Log.w(TAG, "定位权限被拒绝");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog dialog = builder.setTitle("权限说明")
                    .setMessage("请前往设置->应用-><应用名称>->权限中打开相关权限，否则功能无法正常运行！")
                    .setPositiveButton("好的，去授权", new Dialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            try {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton("依旧拒绝", new Dialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "拒绝");
                            dialog.dismiss();
                        }
                    })
                    .create();
            dialog.show();
        } else {
            Log.d(TAG, "定位权限批准");
            doLocation();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED, sticky = true)
    public void onRecvMessage(TestMessage msg) {
        Log.d(TAG, "onRecvMessage: " + msg.toString());
    }

    private static class MyOnClickListener implements OnClickListener {
        private WeakReference<SecondActivity> mActRef;

        private MyOnClickListener(SecondActivity activity) {
            mActRef = new WeakReference<>(activity);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "按钮被点击了");
            SecondActivity activity;
            if (mActRef != null && (activity = mActRef.get()) != null) {
                activity.checkPermissions();
            }
        }
    }

    @Bus(value = EventTag.E1001)
    public void onEvent(){

    }
}
