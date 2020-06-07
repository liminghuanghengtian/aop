package com.liminghuang.demo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.liminghuang.cache.annotation.MemCache;
import com.liminghuang.route.annotation.RouteTarget;
import com.liminghuang.viewfinder.ViewFinder;
import com.liminghuang.viewfinder.annotation.BindView;
import com.liminghuang.viewfinder.annotation.OnClick;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

@RouteTarget(target = "/main", tag = "main_page", domain = "main", scheme = "customnative",
        needLogin = true)
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    static {
        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();
    }

    /**
     * 注解测试
     *
     * @param x
     * @param y
     * @return
     */
    @MemCache(key = "calculate")
    private int calculate(int x, int y) {
        Log.i(TAG, "execute calculate...");
        return x + y;
    }

    @BindView(value = R.id.tv_content)
    private TextView mContentView;

    @OnClick(value = R.id.tv_content)
    public void onContentViewClick(View view) {
        Toast.makeText(this, "id: " + view.getId() + " is clicked", Toast.LENGTH_LONG).show();
    }

    @OnClick(value = R.id.tv_title)
    public void onTitleViewClick() {
        Toast.makeText(this, "title is clicked", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewFinder.inject(this);

        calculate(1, 3);

        async();
        rx();
        startActivity(new Intent(this, SecondActivity.class));

        TestMessage msg = new TestMessage();
        msg.setSeq(new Random().nextInt());
        Log.d(TAG, msg.toString());
        EventBus.getDefault().postSticky(msg);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * eventbus测试
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onMessage(TestMessage msg) {
        Log.d(TAG, "onMessage: " + msg.toString());
        Glide.with(this)
                .load("https://blog.csdn.net/hxl517116279")
                .into(new ImageView(this));
    }

    /**
     * eventbus测试
     *
     * @param obj
     */
    @Subscribe
    public void onNotify(Object obj) {

    }

    /**
     * AsyncTask测试
     */
    private void async() {
        MainAsyncTask task = new MainAsyncTask();
        task.execute(new Integer[]{1, 2, 3}).cancel(true);
    }

    /**
     * rxjava测试
     */
    private void rx() {

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 10; i++) {
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(Object o) throws Exception {
                        if (o instanceof Integer) {
                            return (Integer) o % 2 == 0;
                        }
                        return false;
                    }
                })
                .map(new Function<Object, String>() {
                    @Override
                    public String apply(Object o) throws Exception {
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private static class MainAsyncTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... integers) {
            for (Integer i : integers) {
                Log.i(TAG, "num: " + i);
                if (i % 2 == 0) {
                    publishProgress(i);
                }
            }
            return String.valueOf(integers[0]);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        public void onProgressUpdate(Integer... value) {
            super.onProgressUpdate(value);
        }

        @Override
        protected void onCancelled(String result) {
            super.onCancelled(result);
        }

        @Override
        protected void onCancelled() {
        }
    }
}
