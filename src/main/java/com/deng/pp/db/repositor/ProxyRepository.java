package com.deng.pp.db.repositor;

import com.deng.pp.db.RedisConfiguration;
import com.deng.pp.entity.ProxyEntity;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

/**
 * Created by hcdeng on 17-7-3.
 */

public  class ProxyRepository {

    private static final ProxyRepository REPOSITORY = new ProxyRepository();

    public static ProxyRepository getInstance(){return REPOSITORY;}

    private static final  RedisTemplate<String, ProxyEntity> redisTemplate = RedisConfiguration.getRedisTemplate();

    public void save(ProxyEntity proxy) {
        redisTemplate.opsForValue().set(proxy.getKey(), proxy);
    }


    private ProxyEntity getByKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public ProxyEntity get(String ip, int port){
        return getByKey(ip + ":" +port);
    }

    public ProxyEntity getRandomly(){
        return getByKey(redisTemplate.randomKey());
    }

    public List<ProxyEntity> getList(int num){
        List<ProxyEntity> proxys = new ArrayList<>();

        Set<String> keys = redisTemplate.keys("*");
        Iterator<String> it = keys.iterator();

        if(num < 0)num = keys.size();
        while(it.hasNext() && num-- > 0){
            proxys.add(getByKey(it.next()));
        }

        return proxys;
    }

    public List<ProxyEntity> getAll() {
        return  getList(-1);
    }

    public void delete(ProxyEntity b) {
        deleteByKey(b.getKey());
    }

    public void delete(String ip, int port) {
        deleteByKey(ip+":"+port);
    }

    public void deleteByKey(String key) {
        redisTemplate.opsForValue().getOperations().delete(key);
    }

    public void deleteAll() {
        Set<String> keys = redisTemplate.keys("*");
        Iterator<String> it = keys.iterator();

        while(it.hasNext()){
            redisTemplate.opsForValue().getOperations().delete(it.next());
        }
    }
}
