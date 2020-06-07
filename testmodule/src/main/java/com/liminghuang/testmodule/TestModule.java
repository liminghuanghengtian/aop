package com.liminghuang.testmodule;

import com.liminghuang.route.RouteRule;
import com.liminghuang.route.abstraction.module.AbsRouteModule;
import com.liminghuang.route.annotation.RouteModule;

import java.util.List;

/**
 * Description:
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@RouteModule(domain = "test_module")
public class TestModule extends AbsRouteModule {
    @Override
    public List<RouteRule> collectExtRules() {
        return null;
    }

    @Override
    public String authority() {
        return "test_module";
    }
}
