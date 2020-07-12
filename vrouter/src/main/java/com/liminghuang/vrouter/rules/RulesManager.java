package com.liminghuang.vrouter.rules;

import com.liminghuang.route.abstraction.module.IRouteModule;

import java.util.List;

/**
 * ProjectName: AOP
 * Description:
 * CreateDate: 2020/7/5 3:17 PM
 *
 * @author: <a href="mailto:1569642270@qq.com">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class RulesManager {

    private static volatile RulesManager sManager = null;
    private GeneralTable generalTable;

    public static RulesManager getInstance() {
        if (sManager == null) {
            synchronized (RulesManager.class) {
                if (sManager == null) {
                    sManager = new RulesManager();
                }
            }
        }
        return sManager;
    }

    private RulesManager() {
        generalTable = new GeneralTable();
    }

    public void collectRules(List<IRouteModule> modules) {
        generalTable.makeTable(modules);
    }

    public GeneralTable getGeneralTable() {
        return generalTable;
    }
}
