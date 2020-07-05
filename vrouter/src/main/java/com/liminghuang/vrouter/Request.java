package com.liminghuang.vrouter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * ProjectName: AOP
 * Description: 路由请求.
 * CreateDate: 2020/7/5 8:59 PM
 *
 * @author: <a href="mailto:1569642270@qq.com"">colin</a>
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class Request {
    private IntentCompat mIntentCompat;
    private IDispatcher mDispatcher;
    // TODO: 2020/7/5  

    public static class IntentCompat {
        private Intent intent;
        private int reqCode;
        private Bundle options;

        IntentCompat(Intent intent, int reqCode, Bundle options) {
            this.intent = intent;
            this.reqCode = reqCode;
            this.options = options;
        }
    }

    public static class Builder {
        private Context context;
        private String tag;
        private Bundle params;
        private int flags;
        private Uri uri;
        private int reqCode;
        private Bundle options;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder setParams(Bundle params) {
            this.params = params;
            return this;
        }

        public Builder setReqCode(int reqCode) {
            this.reqCode = reqCode;
            return this;
        }

        public Builder setFlags(int flags) {
            this.flags = flags;
            return this;
        }

        public Builder setOptions(Bundle options) {
            this.options = options;
            return this;
        }

        public Builder setUri(Uri uri) {
            this.uri = uri;
            return this;
        }

        public IntentCompat build() {
            Intent intent = null;
            if (tag != null && tag.length() > 0) {
                intent = VRouter.buildIntent(context, tag, params);
            }
            String url = uri.toString();
            if (url != null && url.length() > 0) {
                intent = VRouter.buildIntent(context, url);
            }

            if (intent != null) {
                intent.addFlags(flags);
            }
            return new IntentCompat(intent, reqCode, options);
        }
    }
}
