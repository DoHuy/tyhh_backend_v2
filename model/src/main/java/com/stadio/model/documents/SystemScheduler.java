package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.concurrent.TimeUnit;

@Data
@Document(collection = "md_system_scheduler")
public class SystemScheduler {

    @Id
    private String id;

    @Field(value = "delay")
    private String delay;

    @Field(value = "time_unit")
    private TimeUnit timeUnit;


}
