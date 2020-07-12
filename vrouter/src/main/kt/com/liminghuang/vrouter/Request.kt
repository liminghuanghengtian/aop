package com.liminghuang.vrouter

import android.content.Context
import android.net.Uri
import android.os.Bundle

/**
 * ProjectName: AOP
 * Description: 路由请求.
 * CreateDate: 2020/7/11 8:26 PM
 * @author: <a href="mailto:liming.huang@tuya.com">colin</a>
 * @version: 3.19.0
 * @since: 3.19.0
 */
class Request(val addressCompat: AddressCompat) {

    class AddressCompat internal constructor(val context: Context, val address: Address, val reqCode: Int, val options: Bundle?)

    class Builder(private val context: Context) {
        private var tag: String? = null
        private var params: Bundle? = null
        private var flags = 0
        private var uri: Uri? = null
        private var reqCode = 0
        private var options: Bundle? = null
        fun setTag(tag: String?): Builder {
            this.tag = tag
            return this
        }

        fun setParams(params: Bundle?): Builder {
            this.params = params
            return this
        }

        fun setReqCode(reqCode: Int): Builder {
            this.reqCode = reqCode
            return this
        }

        fun setFlags(flags: Int): Builder {
            this.flags = flags
            return this
        }

        fun setOptions(options: Bundle?): Builder {
            this.options = options
            return this
        }

        fun setUri(uri: Uri?): Builder {
            this.uri = uri
            return this
        }

        fun build(): Request? {
            var address: Address? = null
            tag?.let {
                if (it.isNotEmpty()) {
                    address = VRouter.buildAddress(context, tag!!, params!!)
                }
            }
            val url = uri?.toString()
            url?.let {
                address = VRouter.buildAddress(context, it)
            }
            if (address != null) {
                address!!.intent?.addFlags(flags)
                address!!.intent?.putExtras(params!!)
                // TODO: 2020/7/11
//                 address!!.intent.setData(uri);
            }

            return address?.let { Request(AddressCompat(context, address!!, reqCode, options)) }
        }
    }
}