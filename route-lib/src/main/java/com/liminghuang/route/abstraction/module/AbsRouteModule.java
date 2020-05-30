package com.liminghuang.route.abstraction.module;

import com.liminghuang.route.RouteRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: Route module 模板.
 *
 * @author <a href="mailto:huanglm@guahao.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbsRouteModule implements IRouteModule {
    @Override
    public List<RouteRule> collectRules() {
        List<RouteRule> rules = new ArrayList<>();
        if (collectExtRules() != null) {
            rules.addAll(collectExtRules());
        }
        return rules;
    }
}
