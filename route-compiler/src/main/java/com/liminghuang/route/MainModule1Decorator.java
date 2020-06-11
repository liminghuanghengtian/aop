package com.liminghuang.route;

import com.liminghuang.route.abstraction.collector.IRouteCollector;
import com.liminghuang.route.abstraction.module.IRouteModule;

import java.util.ArrayList;
import java.util.List;

/**
 * ProjectName: AOP
 * Description: 每个模块对自身的装饰.
 * CreateDate: 2020/6/10 7:34 PM
 *
 * @author: <a href="mailto:1569642270@qq.com"">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class MainModule1Decorator implements IRouteModule {
    private IRouteModule target;

    private MainModule1Decorator(IRouteModule target) {
        this.target = target;
    }

    @Override
    public List<RouteRule> collectRules() {
        List<RouteRule> list = new ArrayList<>();
        IRouteCollector collector =
                null;
        try {
            collector = (IRouteCollector) Class.forName("com.liminghuang.demo.RouteCollector_" + target.authority()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        list.addAll(collector.collectRules());
        list.addAll(target.collectRules());
        return list;
    }

    @Override
    public List<RouteRule> collectExtRules() {
        return target.collectExtRules();
    }

    @Override
    public String authority() {
        return target.authority();
    }
}
