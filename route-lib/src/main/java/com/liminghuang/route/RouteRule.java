package com.liminghuang.route;

import java.net.URI;

/**
 * Description: 路由细则.
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class RouteRule {
    /** 路由模式 */
    private Mode mode;
    /** 路由协议 */
    private String scheme;
    /** 域名-注解的模块名称 */
    private String domain;
    /** 形如：host:port，目前基本使用domain */
    private String authority;
    /** 全限定路径 */
    private String qualified;
    /** 形如：/triple/baidu */
    private String path;
    /** 映射键 */
    private String key;
    /** 统一资源定位符 */
    private URI uri;
    /** 是否需要登录 */
    private boolean needLogin;

    public Mode getMode() {
        return mode;
    }

    public String getScheme() {
        return scheme;
    }

    public String getDomain() {
        return domain;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getQualified() {
        return qualified;
    }

    public String getPath() {
        return path;
    }

    public String getKey() {
        return key;
    }

    public URI getUri() {
        return uri;
    }

    public boolean needLogin() {
        return needLogin;
    }

    public enum Mode {
        NATIVE(Constants.MODE_NATIVE),
        H5(Constants.MODE_H5);

        private String desc;

        Mode(String desc) {
            this.desc = desc;
        }
    }

    public static class Builder {
        /** 路由模式 */
        private Mode mode;
        /** 路由协议 */
        private String scheme;
        /** 域名-注解的模块名称 */
        private String domain;
        /** 形如：host:port，目前基本使用domain */
        private String authority;
        /** 全限定路径 */
        private String qualified;
        /** 形如：/triple/baidu */
        private String path;
        /** 映射键 */
        private String key;
        /** 是否需要登录 */
        private boolean needLogin;

        public Builder setMode(Mode mode) {
            this.mode = mode;
            return this;
        }

        public Builder setScheme(String scheme) {
            this.scheme = scheme;
            return this;
        }

        public Builder setDomain(String domain) {
            this.domain = domain;
            return this;
        }

        public Builder setAuthority(String authority) {
            this.authority = authority;
            return this;
        }

        public Builder setQualified(String qualified) {
            this.qualified = qualified;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        public Builder setNeedLogin(boolean needLogin) {
            this.needLogin = needLogin;
            return this;
        }

        public RouteRule build() {
            RouteRule rule = new RouteRule();
            rule.mode = this.mode;
            rule.scheme = this.scheme;
            rule.domain = this.domain;
            rule.authority = this.authority;
            rule.path = this.path;
            rule.key = this.key;
            rule.qualified = this.qualified;
            rule.needLogin = this.needLogin;
            StringBuilder builder = new StringBuilder();
            // 规范：[scheme:][//authority][path][?query][#fragment]，path自带斜杆"/"
            builder.append(scheme).append("://").append(authority).append(path);
            System.out.println("uri str -> " + builder.toString());
            rule.uri = URI.create(builder.toString());
            return rule;
        }
    }
}
