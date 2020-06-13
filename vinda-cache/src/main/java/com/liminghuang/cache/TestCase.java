package com.liminghuang.cache;

import com.liminghuang.cache.annotation.MemCache;

/**
 * Description:
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class TestCase {
    @MemCache(key = "test")
    public void test() {}

    @MemCache()
    public void test(String num, String pw) {}
}
