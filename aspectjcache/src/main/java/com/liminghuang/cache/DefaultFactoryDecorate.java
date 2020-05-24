package com.liminghuang.cache;

import com.liminghuang.cache.ICache.Factory;

/**
 * Description: 具体装饰类，起到给Component添加职责的功能
 *
 * @author <a href="mailto:huanglm@guahao.com">Adaministrator</a>
 * @version 2.6.0
 * @since 2.6.0
 */
public class DefaultFactoryDecorate extends AbsFactory {
    @Override
    public Factory makeFactory() {
        return new LruCacheFactory();
    }
}
