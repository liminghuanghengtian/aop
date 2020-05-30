package com.liminghuang.route.abstraction.collector;

import com.liminghuang.route.RouteRule;

import java.util.List;

/**
 * Description: 路由表收集器.
 *
 * @author <a href="mailto:huanglm@guahao.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IRouteCollector {
    List<RouteRule> collectRules();
}
