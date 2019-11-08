package com.stadio.model.redisUtils;

import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 01/29/2018.
 */
public interface RedisRepository
{
    void select(int index);

    void hmset(String key, Map<String, String> value);

    Map<String, String> hgetAll(String key);

    void hput(String key, String hashKey, String value);

    String hget(String key, String hashKey);

    Long hdelete(String key, String... hashKeys);

    Set<String> findKeysByPattern(String pattern);

    void expire(String key, long time);
}
