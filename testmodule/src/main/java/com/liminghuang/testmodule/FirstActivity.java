package com.liminghuang.testmodule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.liminghuang.route.annotation.RouteTarget;
import com.liminghuang.vrouter.Call;
import com.liminghuang.vrouter.Callback;
import com.liminghuang.vrouter.Request;
import com.liminghuang.vrouter.Response;
import com.liminghuang.vrouter.VRouter;

/**
 * Description:
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@RouteTarget(tag = "first_page", target = "/first")
public class FirstActivity extends AppCompatActivity {
    private static final String TAG = "FirstActivity";
    private static final int REQ_CODE = 450;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = new Bundle();
        bundle.putString("key", "content");
        Request.Builder reqBuilder = new Request.Builder(this)
                .setTag("second_page")
                .setReqCode(REQ_CODE)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setParams(bundle);
        Request request = reqBuilder.build();
        if (request != null) {
            VRouter.Builder builder1 = new VRouter.Builder();
            builder1.addInterceptor(new SecondActivityInterceptor());
            builder1.
            VRouter vRouter = builder1.build();
            vRouter.newCall(request).dispatch(new Callback() {
                @Override
                public void onFailure(Call call, Exception e) {
                    Log.e(TAG, String.format("onFailure, %s", e.getMessage()));
                }

                @Override
                public void onResponse(Call call, Response response) throws Exception {
                    Log.d(TAG, String.format("onResponse, %b", (response != null && response.isSuccessfully())));
                }
            });
        }
    }
}
