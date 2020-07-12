package com.liminghuang.vrouter.rules;

import androidx.annotation.NonNull;

import com.liminghuang.route.RouteRule;
import com.liminghuang.route.abstraction.module.IRouteModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ProjectName: AOP
 * Description: 路由总表.
 * CreateDate: 2020/7/5 2:37 PM
 *
 * @author: <a href="mailto:1569642270@qq.com">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class GeneralTable {
    private List<RouteRule> mGeneralTable;

    public GeneralTable() {
    }

    public void makeTable(@NonNull List<IRouteModule> allModules) {
        if (allModules != null) {
            List<RouteRule> table = new ArrayList<>();
            for (IRouteModule module : allModules) {
                table.addAll(module.collectRules());
            }
            mGeneralTable = Collections.unmodifiableList(table);
        } else {
            mGeneralTable = Collections.emptyList();
        }
    }

    public List<RouteRule> getRouteTable() {
        return mGeneralTable;
    }
}
