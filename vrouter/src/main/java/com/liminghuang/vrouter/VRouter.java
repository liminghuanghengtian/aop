package com.liminghuang.vrouter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.liminghuang.route.RouteRule;
import com.liminghuang.vrouter.match.impl.KeyMatcher;
import com.liminghuang.vrouter.match.impl.RouterMatcherImpl;
import com.liminghuang.vrouter.match.impl.UrlMatcher;

import java.util.Set;

/**
 * ProjectName: AOP
 * Description: VRouter API.
 * CreateDate: 2020/7/5 6:01 PM
 *
 * @author: <a href="mailto:1569642270@qq.com"">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class VRouter {
    private static final String TAG = "VRouter";
    private static KeyMatcher sKeyMatcher = new KeyMatcher(new RouterMatcherImpl());
    private static UrlMatcher sUrlMatcher = new UrlMatcher(new RouterMatcherImpl());

    public static Intent buildIntent(@NonNull Context context, String tag, @NonNull Bundle params) {
        RouteRule rule = sKeyMatcher.match(tag);
        Intent intent = null;
        if (rule != null) {
            if (RouteRule.Mode.NATIVE == rule.getMode()) {
                intent = new Intent();
                // 明确的组件名称，就不需要系统去匹配intent-filter
                intent.setComponent(new ComponentName(context.getPackageName(), rule.getQualified()));
                intent.putExtras(params);
            } else {
                Log.w(TAG, "need thinking");
            }
        }
        return intent;
    }

    public static Intent buildIntent(Context context, String url) {
        RouteRule rule = sUrlMatcher.match(url);
        Intent intent = null;
        if (rule != null) {
            if (RouteRule.Mode.NATIVE == rule.getMode()) {
                Log.w(TAG, "need thinking");
            } else {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                Uri uri = Uri.parse(url);
                Set<String> paramNames = uri.getQueryParameterNames();
                for (String paramName : paramNames) {
                    intent.putExtra(paramName, uri.getQueryParameter(paramName));
                }
            }
        }
        return intent;
    }
}
