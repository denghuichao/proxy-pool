package com.deng.pp.utils;

import com.deng.pp.entity.ProxyEntity;
import com.deng.pp.fetcher.GoubanjiaFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;

/**
 * Created by hcdeng on 17-7-3.
 */
public class ProxyUtil {

    private static final Logger logger = LoggerFactory.getLogger(ProxyUtil.class);


    private static final String VERIFY_URL = "http://www.baidu.com/";


    public static boolean verifyProxy(ProxyEntity proxy){
       if(proxy == null)return false;
       return verifyProxy(proxy.getIp(), proxy.getPort());
    }


    public static boolean verifyProxy(String proxy){

        if(proxy == null || !proxy.matches("\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+"))
            return false;

        String[] ps = proxy.trim().split(":");
        return verifyProxy(ps[0], Integer.valueOf(ps[1]));
    }


    /**
     * 验证代理是否可用
     * @param ip
     * @param port
     * @return
     */
    public static boolean verifyProxy(String ip, int port ){
        boolean useful;
        try {
            URL url = new URL(VERIFY_URL);
            InetSocketAddress addr = new InetSocketAddress(ip, port);
            System.setProperty("http.proxyHost", ip);
            System.setProperty("http.proxyPort", String.valueOf(port));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(20 * 1000);
            int rCode = connection.getResponseCode();
            useful =  rCode == 200;
        }catch (IOException e1){
            logger.warn(String.format("verify proxy %s:%d exception: "+e1.getMessage(),ip, port));
            useful = false;
        }

        logger.info(String.format("verify proxy %s:%d useful: "+useful, ip, port));

        return useful;
    }
}
