package com.liminghuang.cache;

/**
 * Description: 抽象装饰类，装饰抽象类，继承了Component，即ICache.Factory
 * .从外类来扩展Component类的功能，但对于Component来说，是无需知道Decorator存在的。
 *
 * @author <a href="mailto:huanglm@guahao.com">Adaministrator</a>
 * @version 2.6.0
 * @since 2.6.0
 */
public abstract class FactoryDecorator implements ICache.Factory {
    private final ICache.Factory factory;

    public FactoryDecorator() {
        factory = makeFactory();
    }

    @Override
    public ICache create() {
        if (factory != null) {
            return factory.create();
        }
        return null;
    }

    @Override
    public String name() {
        if (factory != null) {
            return factory.name();
        }
        return "No-name";
    }

    public abstract ICache.Factory makeFactory();
}
