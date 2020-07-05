package com.liminghuang.vrouter.match.impl;

import com.liminghuang.route.RouteRule;
import com.liminghuang.vrouter.match.AbsUrlMatcher;
import com.liminghuang.vrouter.match.IMatcher;

/**
 * ProjectName: AOP
 * Description:
 * CreateDate: 2020/7/5 3:15 PM
 *
 * @author: <a href="mailto:1569642270@qq.com"">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class UrlMatcher extends AbsUrlMatcher {
    public UrlMatcher(IMatcher matcher) {
        super(matcher);
    }

    @Override
    public RouteRule match(String url) {
        return match(url, null);
    }
}
