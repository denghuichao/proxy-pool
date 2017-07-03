package com.deng.utils;

import com.deng.api.Response;
import com.deng.entity.ProxyEntity;

import java.io.IOException;
import java.net.*;

/**
 * Created by hcdeng on 17-7-3.
 */
public class ProxyUtil {

    private static final String VERIFY_URL = "https://www.baidu.com/";
    /**
     * 验证代理是否可用
     * @param proxy
     * @return
     */
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

    public static boolean verifyProxy(String ip, int port ){
        try {
            URL url = new URL(VERIFY_URL);
            InetSocketAddress addr = new InetSocketAddress(ip, port);
            Proxy p = new Proxy(Proxy.Type.HTTP, addr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(p);// 设置代理访问
            connection.setConnectTimeout(20 * 1000);
            int rCode = connection.getResponseCode();
            return  rCode == 200;
        }catch (IOException e1){
            return false;
        }
    }


}
