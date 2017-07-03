package com.deng.pp.schedule;

import com.deng.pp.db.repositor.ProxyRepository;
import com.deng.pp.entity.ProxyEntity;
import com.deng.pp.utils.ProxyUtil;

import java.util.Collection;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by hcdeng on 17-7-3.
 */
public class ProxyVerifier{

    private static final ExecutorService EXEC = Executors.newFixedThreadPool(2);

    private  static final BlockingDeque<ProxyEntity> PROXYS = new LinkedBlockingDeque<>();

    static {start();}

    public static void verifyAndUpdate(ProxyEntity proxy){
        try{
            PROXYS.put(proxy);
        }catch (InterruptedException e){}
    }

    public static void verifyAndUpdateAll(Collection<ProxyEntity> proxys){
        for(ProxyEntity p : proxys)
            try{
                PROXYS.put(p);
            }catch (InterruptedException e){}
    }

    private static void start(){
        while(true){
            try {
                ProxyEntity proxy = PROXYS.take();
                boolean useful = ProxyUtil.verifyProxy(proxy);
                if(useful)
                    ProxyRepository.getInstance().save(proxy);
                else ProxyRepository.getInstance().delete(proxy);
            }catch (InterruptedException e){

            }
        }
    }
}
