package com.deng.pp.schedule;

import com.deng.pp.db.repositor.ProxyRepository;
import com.deng.pp.entity.ProxyEntity;
import com.deng.pp.utils.ProxyUtil;
import com.fasterxml.jackson.annotation.JacksonInject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by hcdeng on 17-7-3.
 */
public class ProxyVerifier {

    private static final int THREAD_NUMS = 4;

    private static final ExecutorService EXEC = Executors.newFixedThreadPool(THREAD_NUMS);

    private static final BlockingDeque<ProxyEntity> PROXYS = new LinkedBlockingDeque<>(100000);

    private static final Logger logger = LoggerFactory.getLogger(ProxyVerifier.class);

    static {
        start();
    }

    public static void verifyAndUpdate(ProxyEntity proxy) {
        try {
            PROXYS.put(proxy);
        } catch (InterruptedException e) {
        }
    }

    public static void verifyAndUpdateAll(Collection<ProxyEntity> proxys) {
        for (ProxyEntity p : proxys)
            try {
                PROXYS.put(p);
            } catch (InterruptedException e) {}
    }

    private static void start() {
        for(int i = 0; i < THREAD_NUMS; i++) {
            EXEC.execute(() -> {
                while (true) {
                    try {
                        ProxyEntity proxy = PROXYS.take();
                        boolean useful = ProxyUtil.verifyProxy(proxy);
                        if (useful)
                            ProxyRepository.getInstance().save(proxy);
                        else ProxyRepository.getInstance().delete(proxy);
                    } catch (InterruptedException e) {
                        logger.info("exception when verifying proxy: " + e.getMessage());
                    }
                }
            });
        }
    }
}
