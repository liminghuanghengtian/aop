package com.liminghuang.cache;

import com.liminghuang.cache.annotation.MemCache;

/**
 * Description:
 *
 * @author <a href="mailto:huanglm@guahao.com">Adaministrator</a>
 * @version 2.6.0
 * @since 2.6.0
 */
public class TestCase {
    @MemCache(key = "test")
    public void test() {}

    @MemCache()
    public void test(String num, String pw) {}
}
