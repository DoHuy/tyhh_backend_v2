package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "tab_popup_news")
public class PopupNews {

    @Id
    private String id;

    @Field(value = "title")
    private String title;

    @Field(value = "desc")
    private String desc;

    @Field(value = "content")
    private String content;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "updated_date")
    private Date updatedDate;

    @Field(value = "created_by")
    private String createdBy;

    @Field(value = "updated_by")
    private String updatedBy;

    @Field(value = "image_id_ref")
    private String imageIdRef;

    @Field(value = "image_url")
    private String imageUrl;

    @Field(value = "show_in_app")
    private boolean showInApp;

    @Field(value = "action_url")
    private String actionUrl;

    public PopupNews()
    {
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }
}
