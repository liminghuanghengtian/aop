package com.liminghuang.demo;

import com.liminghuang.route.RouteRule;
import com.liminghuang.route.abstraction.module.AbsRouteModule;
import com.liminghuang.route.annotation.RouteModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@RouteModule(domain = "app")
public class MainModule extends AbsRouteModule {

    @Override
    public List<RouteRule> collectExtRules() {
        List<RouteRule> rules = new ArrayList<>();
        // https://github.com/liminghuanghengtian/aop
        RouteRule.Builder builder1 = new RouteRule.Builder();
        builder1.setMode(RouteRule.Mode.H5);
        builder1.setScheme("https");
        builder1.setDomain("app");
        builder1.setAuthority("github.com");
        builder1.setPath("/liminghuanghengtian/aop");
        builder1.setKey("aop_page");
        builder1.setQualified("com.liminghuang.demo.GithubAopProjectActivity");
        builder1.setNeedLogin(false);
        rules.add(builder1.build());
        return rules;
    }

    @Override
    public String authority() {
        return "app";
    }
}
