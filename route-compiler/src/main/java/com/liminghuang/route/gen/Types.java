package com.liminghuang.route.gen;

import com.liminghuang.route.abstraction.IModuleGenerator;
import com.liminghuang.route.abstraction.collector.IRouteCollector;
import com.liminghuang.route.abstraction.module.IRouteModule;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

/**
 * Description: 类型常量.
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class Types {
    public static final String ROUTE_RULE = "RouteRule";
    public static final String ROUTE_RULE_BUILDER = "RouteRule.Builder";
    public static final String ROUTE_TABLE_PREFIX = "RouteCollector_";
    public static final String ROUTE_MODULE_SUFFIX = "Decorator";
    public static final String ROUTE_PKG= "com.liminghuang.route";
    public static final String ROUTE_MODULE_COMPOSITION_CLZ_NAME = "ModuleComposition";
    public static final ClassName CLZ_STRING = ClassName.get("java.lang", "String");
    public static final ClassName CLZ_ROUTE_RULE = ClassName.get("com.liminghuang.route", Types.ROUTE_RULE);
    public static final ClassName CLZ_LIST = ClassName.get("java.util", "List");
    public static final ClassName CLZ_ARRAY_LIST = ClassName.get("java.util", "ArrayList");
    public static final ClassName CLZ_IROUTE_COLLECTOR = ClassName.get(IRouteCollector.class);
    public static final ClassName CLZ_IROUTE_MODULE = ClassName.get(IRouteModule.class);
    public static final ClassName CLZ_IMODULE_COMPOSITION = ClassName.get(IModuleGenerator.class);
    public static final TypeName TYPE_LIST_OF_ROUTE_RULE = ParameterizedTypeName.get(Types.CLZ_LIST, Types.CLZ_ROUTE_RULE);
    public static final TypeName TYPE_LIST_OF_MODULE = ParameterizedTypeName.get(Types.CLZ_LIST, Types.CLZ_IROUTE_MODULE);

}
