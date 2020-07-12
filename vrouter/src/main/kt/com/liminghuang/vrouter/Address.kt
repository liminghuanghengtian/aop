package com.liminghuang.vrouter

import android.content.Intent
import com.liminghuang.route.RouteRule

/**
 * ProjectName: AOP
 * Description:
 * CreateDate: 2020/7/11 4:51 PM
 * @author: <a href="mailto:liming.huang@tuya.com">colin</a>
 * @version: 3.19.0
 * @since: 3.19.0
 */
data class Address(val rule: RouteRule, val intent: Intent?) {
    val assembleUrl
        get() = rule.mode.toString() + "://" + rule.authority + rule.path
}