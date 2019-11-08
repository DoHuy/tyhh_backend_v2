package com.stadio.model.redisUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Andy on 01/29/2018.
 */
public class MappingRedis
{
    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> T convertMapToObject(Map<String, String> map, Class<T> ck)
    throws Exception
    {
        String json = mapper.writeValueAsString(map);
        return mapper.readValue(json, ck);
    }

    public static Map<String, String> convertObjectToMap(Object obj)
    throws Exception
    {
        //mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        String json = mapper.writeValueAsString(obj);
        return mapper.readValue(json, new TypeReference<ConcurrentHashMap<String, String>>(){});
    }
}
