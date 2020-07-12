package com.liminghuang.demo;

import android.app.Application;

import com.liminghuang.route.ModuleComposition;
import com.liminghuang.vrouter.rules.RulesManager;

/**
 * ProjectName: AOP
 * Description:
 * CreateDate: 2020/7/12 8:30 PM
 *
 * @author: <a href="mailto:liming.huang@tuya.com">colin</a>
 * @version: 3.19.0
 * @since: 3.19.0
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RulesManager.getInstance().collectRules(new ModuleComposition().getModules());
    }
}
