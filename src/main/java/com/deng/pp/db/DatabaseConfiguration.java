package com.deng.pp.db;

import com.deng.pp.entity.ProxyEntity;
import com.deng.pp.utils.PropsUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.Protocol;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Created by hcdeng on 17-7-3.
 */
@Configuration
public class DatabaseConfiguration {

    private static final Properties REDIS_PROPS = PropsUtil.loadProps("redis.properties");

    @Bean
    public JedisConnectionFactory jedisConnFactory() {
        try {
            String redistogoUrl = REDIS_PROPS.getProperty("redis.url");
            URI redistogoUri = new URI(redistogoUrl);

            JedisConnectionFactory jedisConnFactory = new JedisConnectionFactory();

            jedisConnFactory.setUsePool(true);
            jedisConnFactory.setHostName(redistogoUri.getHost());
            jedisConnFactory.setPort(redistogoUri.getPort());
            jedisConnFactory.setTimeout(Protocol.DEFAULT_TIMEOUT);
            jedisConnFactory.setPassword(redistogoUri.getUserInfo().split(":", 2)[1]);

            return jedisConnFactory;

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        return stringRedisSerializer;
    }

    @Bean
    public JacksonJsonRedisSerializer<ProxyEntity> jacksonJsonRedisJsonSerializer() {
        JacksonJsonRedisSerializer<ProxyEntity> jacksonJsonRedisJsonSerializer = new JacksonJsonRedisSerializer<>(ProxyEntity.class);
        return jacksonJsonRedisJsonSerializer;
    }

    @Bean
    public RedisTemplate<String, ProxyEntity> redisTemplate() {
        RedisTemplate<String, ProxyEntity> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnFactory());
        redisTemplate.setKeySerializer(stringRedisSerializer());
        redisTemplate.setValueSerializer(jacksonJsonRedisJsonSerializer());
        return redisTemplate;
    }
}
