package com.liminghuang.cache;

import android.util.LruCache;

/**
 * ProjectName: data_structure
 * PackageName: com.liminghuang.map
 * Description: 基于LinkedHashMap实现LruCache算法. 主要涉及以下两点修改：
 * <p>1. accessOrder需要设置为true，开启访问排序功能.</p>
 * <p>2. 指定缓存容量，并覆写removeEldestEntry方法，在容量满足时返回true，开启移除eldestNode的功能.</p>
 * <p>
 * CreateTime: 2020/4/17 16:25
 * Modifier: Adaministrator
 * ModifyTime: 2020/4/17 16:25
 * Comment:
 *
 * @author Adaministrator
 */
public class LruCacheImpl implements ICache<String, Object> {
    private static final int MAX_SIZE = 4;
    private LruCache<String, Object> internalCache = new LruCache<>(MAX_SIZE);


    @Override
    public Object put(String key, Object value) {
        return internalCache.put(key, value);
    }

    @Override
    public Object get(String key) {
        return internalCache.get(key);
    }

    @Override
    public Object remove(String key) {
        return internalCache.remove(key);
    }

    @Override
    public void clear() {
        internalCache.evictAll();
    }

    @Override
    public int size() {
        return internalCache.size();
    }

    @Override
    public int missCount() {
        return internalCache.missCount();
    }

    @Override
    public int hitCount() {
        return internalCache.hitCount();
    }
}
