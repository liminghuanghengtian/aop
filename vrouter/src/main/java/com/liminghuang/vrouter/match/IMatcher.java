package com.liminghuang.vrouter.match;

import androidx.annotation.Nullable;

import com.liminghuang.route.RouteRule;

/**
 * ProjectName: AOP
 * Description: 根据url或tag匹配目标路由.
 * CreateDate: 2020/7/5 2:33 PM
 *
 * @author: <a href="mailto:1569642270@qq.com">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
public interface IMatcher {
    @Nullable
    RouteRule match(String url, String tag);
}
