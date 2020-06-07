package com.liminghuang.viewfinder.compiler;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic.Kind;

/**
 * Description: 日志输出.
 *
 * @author <a href="mailto:huanglm@guahao.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
class Logger {
    private Messager mMessager;
    private static Logger sInstance = null;

    private Logger(Messager messager) {
        mMessager = messager;
    }

    static synchronized Logger getInstance(Messager messager) {
        if (sInstance == null) {
            sInstance = new Logger(messager);
        }
        return sInstance;
    }


    void error(String msg) {
        mMessager.printMessage(Kind.ERROR, msg);
    }

    void error(String format, Object... params) {
        mMessager.printMessage(Kind.ERROR, String.format(format, params));
    }

    void info(String format, Object... params) {
        mMessager.printMessage(Kind.NOTE, String.format(format, params));
    }
}
