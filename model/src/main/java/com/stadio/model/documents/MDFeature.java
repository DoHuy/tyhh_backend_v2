package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "md_feature")
@Data
public class MDFeature {

    @Id
    private String id;
    private String name;
    private String path;
    @Field(value = "feature_id")
    private String featureId;
    private String controller;
    private String method;
    private String hash;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "updated_date")
    private Date updatedDate;

    public MDFeature() {
        this.updatedDate = new Date();
        this.createdDate = new Date();
    }
}
