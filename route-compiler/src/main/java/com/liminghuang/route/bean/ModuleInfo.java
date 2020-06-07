package com.liminghuang.route.bean;

import java.util.Objects;

/**
 * Description:
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class ModuleInfo {
    /** 注解所在类的全限定路径类名. */
    private String qualified;
    /** 注解所在类的名称. */
    private String simpleName;
    /** 域名. */
    private String domain;

    public String getQualified() {
        return qualified;
    }

    public void setQualified(String qualified) {
        this.qualified = qualified;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModuleInfo)) {
            return false;
        }
        ModuleInfo that = (ModuleInfo) o;
        return getDomain().equals(that.getDomain());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDomain());
    }

    @Override
    public String toString() {
        return "ModuleInfo{" +
                "qualified='" + qualified + '\'' +
                ", simpleName='" + simpleName + '\'' +
                ", domain='" + domain + '\'' +
                '}';
    }
}
