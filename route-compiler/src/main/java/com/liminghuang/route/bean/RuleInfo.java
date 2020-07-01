package com.liminghuang.route.bean;

import com.liminghuang.route.annotation.RouteTarget;

import java.util.Objects;

/**
 * Description: {@link RouteTarget}注解信息解析存储.
 * 描述如下路由地址信息.
 * <p>模板：{scheme}://{domain}/{path}?{query}</p>
 * <p>url语法格式：[scheme:][//authority][path][?query][#fragment]</p>
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class RuleInfo {
    /** 路由协议 */
    private String scheme;
    /** 域名-模块名称 */
    private String domain;
    /** 包名 */
    private String packName;
    /** 全限定路径 */
    private String qualified;
    /** 形如：/triple/baidu */
    private String path;
    /** 形如：？clientId=sdafaf&name=afghaggs */
    private String query;
    /** 映射键 */
    private String key;
    /** 是否需要登录 */
    private boolean needLogin;

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getQualified() {
        return qualified;
    }

    public void setQualified(String qualified) {
        this.qualified = qualified;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        this.needLogin = needLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RuleInfo)) {
            return false;
        }
        RuleInfo rule = (RuleInfo) o;
        return (getScheme().equals(rule.getScheme()) &&
                getDomain().equals(rule.getDomain()) &&
                getQualified().equals(rule.getQualified()) &&
                getPath().equals(rule.getPath())) || getKey().equals(rule.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), getScheme(), getDomain(), getPath());
    }

    @Override
    public String toString() {
        return "RuleInfo{" +
                "scheme='" + scheme + '\'' +
                ", domain='" + domain + '\'' +
                ", packName='" + packName + '\'' +
                ", qualified='" + qualified + '\'' +
                ", path='" + path + '\'' +
                ", query='" + query + '\'' +
                ", key='" + key + '\'' +
                ", needLogin=" + needLogin +
                '}';
    }
}
