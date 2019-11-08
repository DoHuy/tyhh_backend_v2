package com.stadio.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Andy on 11/10/2017.
 */
public class JsonUtils
{
    private static ObjectMapper mapper = new ObjectMapper();
    private static Logger logger = LogManager.getLogger(JsonUtils.class);

    public static String pretty(Object obj) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        }
        catch (Exception e) {
            logger.error("Parsing pretty exception: " + e);
        }
        return obj.toString();
    }

    public static <T> T parse(String value, Class<T> typeParameterClass)
    {
        try
        {
            return mapper.readValue(value, typeParameterClass);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    public static String writeValue(Object obj)
    {
        try
        {
            return mapper.writeValueAsString(obj);
        }
        catch (Exception e)
        {
            logger.error("WriteValue Exception:", e);
        }
        return null;
    }

    public static Map<String, String> convertJsonToMap(String json)
    throws IOException
    {
        return mapper.readValue(json, new TypeReference<ConcurrentHashMap<String, String>>(){});
    }

    public static<T1> T1 convertJsonToObject(String json, Class<T1> cl)
    throws IOException
    {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(json, cl);
    }
}
