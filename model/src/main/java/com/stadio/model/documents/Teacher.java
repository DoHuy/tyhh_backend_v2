package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "tab_teacher")
public class Teacher {
    @Id
    private String id;

    @Field(value = "user_id_ref")
    private String userId;

    @Field(value = "name")
    private String name;

    @Field(value = "age")
    private Integer age;

    @Field(value = "phone")
    private String phone;

    @Field(value = "subject")
    private String subject;

    @Field(value = "picture_url")
    private String pictureUrl;

    @Field(value = "description")
    private String description;

    @Field(value = "school")
    private String school;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "deleted")
    private boolean deleted;

    public Teacher(){
        this.createdDate = new Date();
        this.deleted = false;
    }
}
