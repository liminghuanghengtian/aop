package com.liminghuang.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.liminghuang.route.annotation.RouteTarget;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 所有以on开头的方法都会被注入打印方法的代码.
 */
@RouteTarget(target = "/second", tag = "second_page", needLogin = false)
public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "SecondActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        TextView tv = findViewById(R.id.tv_content);
        // fixme 以下的方法调用会被切面替换掉执行逻辑
        tv.setOnClickListener(new MyOnClickListener());
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
        @Override
        public void onClick(View v) {
            Log.d(TAG, "按钮被点击了");
        }
    }
}
