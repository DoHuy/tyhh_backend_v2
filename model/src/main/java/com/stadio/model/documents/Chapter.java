package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Data
@Document(collection = "tab_chapter")
public class Chapter implements Serializable {
    @Id
    private String id;

    @Field(value = "name")
    private String name;

    @Field(value = "code")
    private String code;

    @Field(value = "description")
    private String description;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "deleted")
    private boolean deleted;

    public Chapter(){
        this.createdDate = new Date();
        this.deleted=false;
    }
}
