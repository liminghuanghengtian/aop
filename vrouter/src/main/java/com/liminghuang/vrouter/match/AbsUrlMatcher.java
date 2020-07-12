package com.liminghuang.vrouter.match;


import com.liminghuang.route.RouteRule;

/**
 * ProjectName: AOP
 * Description:
 * CreateDate: 2020/7/5 2:48 PM
 *
 * @author: <a href="mailto:1569642270@qq.com">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
public abstract class AbsUrlMatcher extends AbsMatcher {
    public AbsUrlMatcher(IMatcher matcher) {
        super(matcher);
    }

    public abstract RouteRule match(String url);
}
