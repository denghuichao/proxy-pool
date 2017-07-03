package com.deng.pp.schedule;

import com.deng.pp.db.repositor.ProxyRepository;
import com.deng.pp.entity.ProxyEntity;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by hcdeng on 2017/6/29.
 */
public class VerifyScheduler extends Scheduler {

    @Inject
    private ProxyRepository usefulProxyRepository;

    public VerifyScheduler(long defaultInterval, TimeUnit defaultUnit) {
        super(defaultInterval, defaultUnit);
    }

    @Override
    public void run() {
        List<ProxyEntity> proxys = usefulProxyRepository.getAll();
        ProxyVerifier.verifyAndUpdateAll(proxys);
    }
}
