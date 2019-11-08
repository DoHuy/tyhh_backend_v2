package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Andy on 12/23/2017.
 */
@Document(collection = "tab_image")
@Data
public class Image implements Serializable
{
    @Id
    private String id;

    @Field(value = "url")
    private String url;

    @Field(value = "uri")
    private String uri;

    @Field(value = "url_thumb")
    private String urlThumb;

    @Field(value = "name")
    private String name;

    @Field(value = "description")
    private String description;

    @Field(value = "width")
    private double width;

    @Field(value = "height")
    private double height;

    @Field(value = "created_date")
    @Indexed(name = "created_date", direction = IndexDirection.DESCENDING)
    @CreatedDate
    private Date createdDate;

    public Image() {
        this.createdDate = new Date();
    }

}
