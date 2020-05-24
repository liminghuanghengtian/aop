package com.liminghuang.cache;

/**
 * Description:
 *
 * @author <a href="mailto:huanglm@guahao.com">Adaministrator</a>
 * @version 2.6.0
 * @since 2.6.0
 */
public interface ICache<K, V> {
    V put(K key, V value);

    V get(K key);

    V remove(K key);

    void clear();

    int size();

    int missCount();

    int hitCount();

    /**
     * Component: 抽象构件。定义一个对象接口
     */
    interface Factory {
        ICache createCache();

        String name();
    }
}
