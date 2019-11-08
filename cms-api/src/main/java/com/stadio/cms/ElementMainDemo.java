package com.stadio.cms;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ElementMainDemo {


    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        File f = new File(ElementMainDemo.class.getClassLoader().getResource("imports/elements_demo.json").getFile());
        Map<String, Object> results = mapper.readValue(f, HashMap.class);
        System.out.println(results.size());
        for (String key: results.keySet()) {
            System.out.println("@JsonProperty(value = \"" + key +"\")");
            System.out.println("@Field(value = \"" + key + "\")");
            key = upTo(key);
            System.out.println("private String " + key + ";\n");
        }
    }

    private static String upTo(String key) {

        String[] arr = key.split("_");
        if (arr.length == 0 || arr.length == 1 || key.startsWith("_")) {
            return key.replace("_", "");
        } else {
            for (int i = 0; i < arr.length; i++) {
                if (i > 0) {
                    String s = arr[i];
                    arr[i] = s.replace(String.valueOf(s.charAt(0)), String.valueOf(s.charAt(0)).toUpperCase());
                }
            }
            return StringUtils.join(arr);
        }


    }
}
