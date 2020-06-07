package com.liminghuang.viewfinder;

import android.app.Activity;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 视图查找器.
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class ViewFinder {
    private static final ActivityProvider PROVIDER_ACTIVITY = new ActivityProvider();
    private static final ViewProvider PROVIDER_VIEW = new ViewProvider();
    /** 缓存注解类对应的查找器. */
    private static final Map<String, Finder> FINDER_MAP = new HashMap<>();

    /**
     * for activity.
     *
     * @param activity
     */
    public static void inject(Activity activity) {
        inject(activity, activity, PROVIDER_ACTIVITY);
    }

    /**
     * for view.
     *
     * @param view
     */
    public static void inject(View view) {
        inject(view, view);
    }

    /**
     * for fragment.
     *
     * @param host
     * @param view
     */
    public static void inject(Object host, View view) {
        inject(host, view, PROVIDER_VIEW);
    }

    public static void inject(Object host, Object source, Provider provider) {
        String className = host.getClass().getName();
        try {
            Finder finder = FINDER_MAP.get(className);
            if (finder == null) {
                Class<?> finderClass = Class.forName(className + "$$Finder");
                finder = (Finder) finderClass.newInstance();
                FINDER_MAP.put(className, finder);
            }
            // 执行注入逻辑.
            finder.inject(host, source, provider);
        } catch (Exception e) {
            throw new RuntimeException("Unable to inject for " + className, e);
        }
    }

    private static class ActivityProvider implements Provider {
        @Override
        public View findView(Object source, int id) {
            if (source instanceof Activity) {
                return ((Activity) source).findViewById(id);
            } else {
                throw new IllegalArgumentException(String.format("source{%S} is not Activity",
                        source));
            }
        }
    }

    private static class ViewProvider implements Provider {
        @Override
        public View findView(Object source, int id) {
            // TODO: 2020/6/7
            return null;
        }
    }
}
