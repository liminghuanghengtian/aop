package com.liminghuang.route.abstraction.module;

import com.liminghuang.route.RouteRule;
import com.liminghuang.route.abstraction.collector.IRouteCollector;

import java.util.List;

/**
 * Description: 路由模块抽象.
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IRouteModule extends IRouteCollector {

    /**
     * 上层嵌入路由.
     *
     * @return
     */
    List<RouteRule> collectExtRules();

    /**
     * 模块域名.
     *
     * @return
     */
    String authority();
}
