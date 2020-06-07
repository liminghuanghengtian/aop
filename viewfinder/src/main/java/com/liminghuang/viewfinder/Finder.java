package com.liminghuang.viewfinder;


/**
 * Description: 抽象查找器.
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Finder<T> {
    /**
     * ioc注入.
     *
     * @param host     注解所在宿主类.
     * @param source   查找的来源.
     * @param provider 来源的提供逻辑.
     */
    void inject(T host, Object source, Provider provider);
}
