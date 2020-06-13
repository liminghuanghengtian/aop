package com.liminghuang.cache;

/**
 * Description: 抽象装饰类，装饰抽象类，继承了Component，即ICache.Factory
 * .从外类来扩展Component类的功能，但对于Component来说，是无需知道Decorator存在的。
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbsFactory implements ICache.Factory {
    private final ICache.Factory mActualFactory;

    public AbsFactory() {
        mActualFactory = makeFactory();
    }

    @Override
    public ICache createCache() {
        if (mActualFactory != null) {
            return mActualFactory.createCache();
        }
        return null;
    }

    @Override
    public String name() {
        if (mActualFactory != null) {
            return mActualFactory.name();
        }
        return "No-name";
    }

    public abstract ICache.Factory makeFactory();
}
