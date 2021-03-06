package com.liminghuang.cache;

import com.liminghuang.cache.ICache.Factory;

/**
 * Description: 具体装饰类，起到给Component添加职责的功能
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class DefaultFactoryDecorate extends AbsFactory {
    @Override
    public Factory makeFactory() {
        return new LruCacheFactory();
    }
}
