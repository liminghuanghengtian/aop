package com.liminghuang.route.abstraction;

import com.liminghuang.route.abstraction.module.IRouteModule;

import java.util.List;

/**
 * Description:
 *
 * @author <a href="mailto:huanglm@guahao.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IModuleGenerator {
    List<IRouteModule> getModules();
}
