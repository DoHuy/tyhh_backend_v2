package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "tab_clazz")
public class Clazz implements Serializable {
    @Id
    private String id;

    @Field(value = "name")
    private String name;

    @Field(value = "description")
    private String description;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "deleted")
    private boolean deleted;

    @Field(value = "chapter_id_ref")
    private List<String> idChapters;

    public Clazz(){
        this.createdDate = new Date();
        this.deleted=false;
    }
}
