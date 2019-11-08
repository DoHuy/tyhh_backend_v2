package com.stadio.common.service.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:storage.properties")
@ConfigurationProperties()
@Component
public class StorageProperties
{

    @Value("${storage.location}")
    private String location;

    @Value("${storage.subject}")
    private String subject;

    public String getLocation() {
        return location+"/"+subject;
    }

    public void setLocation(String location) {
        this.location = location+"/"+subject;
    }

}
