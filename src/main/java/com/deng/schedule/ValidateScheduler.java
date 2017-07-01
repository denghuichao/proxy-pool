package com.deng.schedule;

import java.util.concurrent.TimeUnit;

/**
 * Created by hcdeng on 2017/6/29.
 */
public class ValidateScheduler extends Scheduler {

    public ValidateScheduler(long defaultInterval, TimeUnit defaultUnit) {
        super(defaultInterval, defaultUnit);
    }

    @Override
    public void run() {
        //MemDbUtil.clearUsefulProxys();
        //MemDbUtil.addUsefulProxy(MemDbUtil.getAllRawProxys());
    }
}
