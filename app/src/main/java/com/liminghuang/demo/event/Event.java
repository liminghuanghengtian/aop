package com.liminghuang.demo.event;

import android.os.Message;

/**
 * Created by baixiaokang on 16/11/15.
 */

public interface Event {
    void call(Message msg);
}
