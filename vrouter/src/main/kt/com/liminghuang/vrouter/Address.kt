package com.liminghuang.vrouter

import android.content.Intent
import com.liminghuang.route.RouteRule

/**
 * ProjectName: AOP
 * Description:
 * CreateDate: 2020/7/11 4:51 PM
 * @author: <a href="mailto:1569642270@qq.com">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
data class Address(val rule: RouteRule, val intent: Intent?) {
    val assembleUrl
        get() = rule.mode.toString() + "://" + rule.authority + rule.path
}