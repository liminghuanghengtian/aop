package com.liminghuang.cache;

import android.util.Log;

/**
 * Description: ConcreteComponent:具体构件。是定义了一个具体的对象，也可以给这个对象添加一些职责。
 *
 * @author <a href="mailto:huanglm@guahao.com">Adaministrator</a>
 * @version 2.6.0
 * @since 2.6.0
 */
public class LruCacheFactory implements ICache.Factory {
    private static final String TAG = "LruCacheFactory";

    public LruCacheFactory() {
        Log.d(TAG, "ICache.Factory 实现类： " + name());
    }

    @Override
    public ICache create() {
        return new LruCacheImpl();
    }

    @Override
    public String name() {
        return TAG;
    }
}
