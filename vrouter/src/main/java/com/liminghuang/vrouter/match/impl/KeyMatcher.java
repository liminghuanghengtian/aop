package com.liminghuang.vrouter.match.impl;

import com.liminghuang.route.RouteRule;
import com.liminghuang.vrouter.match.AbsKeyMatcher;
import com.liminghuang.vrouter.match.IMatcher;

/**
 * ProjectName: AOP
 * Description:
 * CreateDate: 2020/7/5 3:06 PM
 *
 * @author: <a href="mailto:1569642270@qq.com"">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class KeyMatcher extends AbsKeyMatcher {
    public KeyMatcher(IMatcher matcher) {
        super(matcher);
    }

    @Override
    public RouteRule match(String tag) {
        return match(null, tag);
    }
}
