package com.deng.pp.schedule;

import com.deng.pp.db.repositor.ProxyRepository;
import com.deng.pp.entity.ProxyEntity;
import com.deng.pp.utils.ProxyUtil;
import com.fasterxml.jackson.annotation.JacksonInject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.*;

/**
 * Created by hcdeng on 17-7-3.
 */
public class ProxyVerifier {

    private static final int THREAD_NUMS = 2;

    private static final ExecutorService EXEC = Executors.newFixedThreadPool(THREAD_NUMS);

    private static final BlockingQueue<ProxyEntity> FETCHED_PROXYS = new LinkedBlockingQueue<>(100000);

    private static final BlockingQueue<ProxyEntity> CACHED_PROXYS = new LinkedBlockingQueue<>(10000);

    private static final Logger logger = LoggerFactory.getLogger(ProxyVerifier.class);

    private static boolean running = true;

    static {start();}

    public static void verify(ProxyEntity proxy) {
        try {
            FETCHED_PROXYS.put(proxy);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void verifyAll(Collection<ProxyEntity> proxys) {
        for (ProxyEntity p : proxys)
            verify(p);
    }

    public static void refresh(ProxyEntity proxy) {
        try {
            CACHED_PROXYS.put(proxy);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void refreshAll(Collection<ProxyEntity> proxys) {
        for (ProxyEntity p : proxys)
            refresh(p);
    }

    /**
     * 启动两个任务,一个验证redis已缓存的代理可用性,一个验证新拉取的代理可用性
     */
    private static void start() {
        startVerify(CACHED_PROXYS, "CACHED_PROXYS");
        startVerify(FETCHED_PROXYS, "FETCHED_PROXYS");
    }

    public static void stop(){running = false;}

    private static void startVerify(final BlockingQueue<ProxyEntity> proxys, final String pName) {
        EXEC.execute(() -> {
            while (running) {
                try {
                    ProxyEntity proxy = proxys.take();
                    logger.info("verifying : " + proxy.getIp()+":"+proxy.getPort()+" in "+pName+", remaining: "+proxys.size());

                    boolean useful = ProxyUtil.verifyProxy(proxy);
                    if (useful)
                        ProxyRepository.getInstance().save(proxy);
                    else ProxyRepository.getInstance().delete(proxy);
                } catch (InterruptedException e) {
                    logger.warn("exception when verifying proxy: " + e.getMessage());
                }
            }
        });
    }
}
