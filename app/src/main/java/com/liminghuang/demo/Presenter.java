package com.liminghuang.demo;

import com.liminghuang.demo.utils.LogUtils;
import com.liminghuang.javassist.lib.annotaion.Bus;
import com.liminghuang.javassist.lib.annotaion.BusRegister;
import com.liminghuang.javassist.lib.annotaion.BusUnRegister;

/**
 * Description:
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class Presenter {
    private static final String TAG = "Presenter";

    @BusRegister
    public void onAttach() {
        LogUtils.d(TAG, "onAttach");
    }

    @BusUnRegister
    public void onDetach() {}

    @Bus(value = EventTag.E1002)
    public void onEvent() {

    }
}
