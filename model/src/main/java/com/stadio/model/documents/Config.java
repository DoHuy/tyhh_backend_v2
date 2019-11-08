package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "tab_config")
public class Config {

    @Id
    @Field(value = "id")
    private String id;

    @Field(value = "key")
    private String key;

    @Field(value = "value")
    private String value;

    @Field(value = "name")
    private String name;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "updated_date")
    private Date updatedDate;

    public Config() {
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }

}
