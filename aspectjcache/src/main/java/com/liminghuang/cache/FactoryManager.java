package com.liminghuang.cache;

/**
 * Description:
 *
 * @author <a href="mailto:huanglm@guahao.com">Adaministrator</a>
 * @version 2.6.0
 * @since 2.6.0
 */
public final class FactoryManager {
    private static volatile FactoryManager sInstance = null;
    private ICache.Factory mFactory;

    public static FactoryManager getInstance() {
        if (sInstance == null) {
            synchronized (FactoryManager.class) {
                if (sInstance == null) {
                    sInstance = new FactoryManager();
                }
            }
        }
        return sInstance;
    }

    public synchronized void setFactory(ICache.Factory factory) {
        if (factory != null) {
            mFactory = factory;
        }
    }

    public synchronized ICache.Factory getFactory() {
        if (mFactory == null) {
            mFactory = new DefaultFactoryDecorate();
        }
        return mFactory;
    }
}
