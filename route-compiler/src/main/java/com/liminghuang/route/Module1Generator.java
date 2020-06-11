package com.liminghuang.route;

import com.liminghuang.route.abstraction.IModuleGenerator;
import com.liminghuang.route.abstraction.module.IRouteModule;

import java.util.ArrayList;
import java.util.List;

/**
 * ProjectName: AOP
 * Description: 需要apt生成的类. 上层反射这个类，获取完整的路由
 * CreateDate: 2020/6/10 7:22 PM
 *
 * @author: <a href="mailto:1569642270@qq.com"">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class Module1Generator implements IModuleGenerator {
    @Override
    public List<IRouteModule> getModules() {
        List<IRouteModule> modules = new ArrayList<>();
        try {
            modules.add((IRouteModule) (Class.forName("com.liminghuang.demo.MainModuleProxy").newInstance()));
            modules.add((IRouteModule) (Class.forName("com.liminghuang.testmodule.TestModuleProxy").newInstance()));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return modules;
    }
}
