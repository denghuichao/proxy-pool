package com.deng.db;

import com.deng.Constants;
import com.deng.utils.PropsUtil;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Properties;

/**
 * Created by hcdeng on 2017/6/30.
 */
public class RedisClient {

    private static final Properties redisProps = PropsUtil.loadProps(Constants.REDIS_PROPS);

    public RedisConnectionFactory getConnectionfactory() {

        JedisConnectionFactory cf = new JedisConnectionFactory();
        cf.setHostName(redisProps.getProperty("hostname","localhost"));
        cf.setPort(PropsUtil.getNumber(redisProps, "port", 7379));

        if(redisProps.containsKey("password"))
            cf.setPassword(redisProps.getProperty("password"));

        return cf;
    }

    public <T> RedisTemplate <String, T> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, T> redis = new RedisTemplate<String, T>();
        redis.setConnectionFactory(cf);
        return redis;
    }
}
