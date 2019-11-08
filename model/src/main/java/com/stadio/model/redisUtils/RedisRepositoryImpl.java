package com.stadio.model.redisUtils;

import com.stadio.model.redisUtils.RedisRepository;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Andy on 01/29/2018.
 */

@Component
public class RedisRepositoryImpl implements RedisRepository
{
    private RedisTemplate redisTemplate;
    private HashOperations<String, String, String> hashOps;
    private ListOperations<String,String> listOps;

    public RedisRepositoryImpl(RedisTemplate redisTemplate)
    {
        this.redisTemplate = redisTemplate;
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
        this.redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        this.redisTemplate.setValueSerializer(new StringRedisSerializer());
        this.redisTemplate.setHashValueSerializer(new StringRedisSerializer());
    }

    @PostConstruct
    private void init()
    {
        hashOps = this.redisTemplate.opsForHash();
        listOps = this.redisTemplate.opsForList();
    }

    @Override
    public void select(int index)
    {
        JedisConnectionFactory redisConnectionFactory = (JedisConnectionFactory)redisTemplate.getConnectionFactory();
        redisConnectionFactory.setDatabase(index);
    }

    @Override
    public void hmset(String key, Map<String, String> value)
    {
        hashOps.putAll(key, value);
    }

    @Override
    public Map<String, String> hgetAll(String key)
    {
        Map<String, String> map = hashOps.entries(key);
        return map;
    }

    @Override
    public void hput(String key, String hashKey, String value) {
        hashOps.put(key, hashKey, value);
    }

    @Override
    public String hget(String key, String hashKey) {
        return hashOps.get(key,hashKey);
    }

    @Override
    public Long hdelete(String key, String... hashKeys){
        return hashOps.delete(key, hashKeys);
    }

    @Override
    public Set<String> findKeysByPattern(String pattern)
    {
        return null;
    }

    @Override
    public void expire(String key, long time)
    {
        this.redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }
}
