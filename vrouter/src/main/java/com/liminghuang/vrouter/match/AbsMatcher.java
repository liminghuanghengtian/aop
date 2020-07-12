package com.liminghuang.vrouter.match;

import androidx.annotation.Nullable;

import com.liminghuang.route.RouteRule;

/**
 * ProjectName: AOP
 * Description:
 * CreateDate: 2020/7/5 3:10 PM
 *
 * @author: <a href="mailto:1569642270@qq.com">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
public abstract class AbsMatcher implements IMatcher {
    private IMatcher matcherImpl;

    public AbsMatcher(IMatcher matcher) {
        matcherImpl = matcher;
    }

    @Nullable
    @Override
    public RouteRule match(String url, String tag) {
        if (matcherImpl != null) {
            matcherImpl.match(url, tag);
        }
        return null;
    }
}
