package com.stadio.mobi.payment;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Andy on 03/02/2018.
 */
public class ParamsBuilder
{
    private Map<String, String> res = new LinkedHashMap<String, String>();

    public void put(String k, String v)
    {
        res.put(k, v);
    }

    public String toParams()
    {
        String s = res.toString();
        s = s.replaceAll(", ", "&");
        s = s.replaceAll("[{,}]", "");
        return s;
    }
}
