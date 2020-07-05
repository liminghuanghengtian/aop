package com.liminghuang.vrouter.match.impl;

import android.net.Uri;

import com.liminghuang.route.RouteRule;
import com.liminghuang.vrouter.match.IMatcher;
import com.liminghuang.vrouter.rules.GeneralTable;
import com.liminghuang.vrouter.rules.RulesManager;

import java.util.Iterator;

/**
 * ProjectName: AOP
 * Description:
 * CreateDate: 2020/7/5 2:50 PM
 *
 * @author: <a href="mailto:1569642270@qq.com"">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class RouterMatcherImpl implements IMatcher {
    @Override
    public RouteRule match(String url, String key) {
        if (url != null && url.trim().length() > 0) {
            return iteratorByUrl(url);
        }
        if (key != null && key.trim().length() > 0) {
            return iteratorByKey(key);
        }
        return null;
    }

    private RouteRule iteratorByKey(String key) {
        GeneralTable table = RulesManager.getInstance().getGeneralTable();
        Iterator<RouteRule> iterator = table.getRouteTable().iterator();
        while (iterator.hasNext()) {
            RouteRule routeRule = iterator.next();
            if (routeRule.getKey().equals(key)) {
                return routeRule;
            }
        }
        return null;
    }

    private RouteRule iteratorByUrl(String url) {
        Uri uri = Uri.parse(url);
        GeneralTable table = RulesManager.getInstance().getGeneralTable();
        Iterator<RouteRule> iterator = table.getRouteTable().iterator();
        while (iterator.hasNext()) {
            RouteRule routeRule = iterator.next();
            if (routeRule.getScheme().equals(uri.getScheme()) &&
            routeRule.getAuthority().equals(uri.getAuthority())) {
                return routeRule;
            }
        }
        return null;
    }
}
