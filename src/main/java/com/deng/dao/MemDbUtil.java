/*
 * Create Author  : huichaodeng
 * Create Date    : 2017-07-01
 * Project        : agent-pool
 * File Name      : MemDb.java
 */
package com.deng.dao;

import com.deng.entity.Proxy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能描述:  <p>
 *
 * @author : huichaodeng <p>
 * @version 1.0 2017-07-01
 * @since agent-pool 1.0
 */
public class MemDbUtil {

    private final static Map<String, Proxy> usefulProxys = new ConcurrentHashMap<>();
    private final static Map<String, Proxy> rawProxys = new ConcurrentHashMap<>();

    public static List<Proxy> getAllRawProxys() {
        return new ArrayList<>(rawProxys.values());
    }

    public static void clearRawProxys() {
        rawProxys.clear();
    }

    public static void addRawProxys(List<? extends Proxy> proxys) {
        for (Proxy proxy : proxys) {
            rawProxys.putIfAbsent(getKey(proxy), proxy);
        }
    }

    public static void refreshRawProxys(List<? extends Proxy> proxys) {
        clearRawProxys();
        addRawProxys(proxys);
    }

    public static void addUsefulProxy(Proxy proxy) {
        usefulProxys.putIfAbsent(getKey(proxy), proxy);
    }

    public static void addUsefulProxy(List<? extends Proxy> proxys) {
        for (Proxy proxy : proxys)
            addUsefulProxy(proxy);
    }

    public static void deleteUsefulProxy(Proxy proxy) {
        deleteUsefulProxy(getKey(proxy));
    }

    public static void clearUsefulProxys(){
        usefulProxys.clear();
    }

    public static void deleteUsefulProxy(String key) {
        usefulProxys.remove(key);
    }

    public static Proxy getAUsefulProxy() {
        Iterator<Proxy> vs = usefulProxys.values().iterator();
        if (!vs.hasNext()) return null;
        return vs.next();
    }

    public static List<Proxy> getUsefulProxys(int size) {
        List<Proxy> list = new ArrayList<>();
        Iterator<Proxy> vs = usefulProxys.values().iterator();
        while (vs.hasNext() && size-- > 0)
            list.add(vs.next());

        return list;
    }

    private static String getKey(Proxy proxy) {
        return proxy.getIp() + ":" + proxy.getPort();
    }
}
