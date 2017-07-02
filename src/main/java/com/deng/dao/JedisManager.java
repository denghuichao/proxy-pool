package com.deng.dao;

import com.deng.utils.PropsUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by hcdeng on 2017/4/7.
 */
public class JedisManager {

    private final static Properties REDIS_PROP = PropsUtil.loadProps("redis.properties");
    private static final Map<String, JedisPool> poolMap = new HashMap<>();

    private JedisManager() {
    }

    public JedisPool getJedisPool(String ip, int port) {
        String key = ip + ":" + port;

        JedisPool pool;

        if (!poolMap.containsKey(key)) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setTestOnBorrow(true);
            config.setTestOnReturn(true);
            pool = new JedisPool(config, ip, port);

            poolMap.put(key, pool);

        } else pool = poolMap.get(key);

        return pool;
    }

    public JedisPool getJedisPool() {
        return getJedisPool("127.0.0.1", 6379);
    }


    public Jedis getJedis(String ip, int port) {
        Jedis jedis = null;
        int count = 0;
        do {
            try {
                jedis = getJedisPool(ip, port).getResource();
            } catch (Exception e) {
                if (jedis != null) jedis.close();
            }
            count++;
        }
        while (jedis == null && count < 3);

        return jedis;
    }

    public void closeJedis(Jedis jedis) {
        if (jedis != null) jedis.close();
    }

    public static JedisManager getInstance() {
        return JediManagerHolder.INSTANCE;
    }

    private static class JediManagerHolder {
        private static JedisManager INSTANCE = new JedisManager();
    }
}
