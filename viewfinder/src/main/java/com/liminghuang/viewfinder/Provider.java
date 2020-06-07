package com.liminghuang.viewfinder;

import android.view.View;

/**
 * Description:
 *
 * @author <a href="mailto:huanglm@guahao.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Provider {
    /**
     * 从来源查找指定视图id对应的视图.
     *
     * @param source
     * @param id
     * @return
     */
    View findView(Object source, int id);
}
