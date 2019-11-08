package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by Andy on 03/04/2018.
 */
@Data
@Document(collection = "tab_message")
public class Message
{
    @Id
    private String id;

    @Field(value = "title")
    private String title;

    @Field(value = "content")
    private String content;

    @Field(value = "user_id")
    @Indexed
    private String userId;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "updated_date")
    private Date updatedDate;

    public Message()
    {
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }
}
