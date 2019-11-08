package com.stadio.model.documents;

import com.stadio.common.enu.DeepLink;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Data
@Document(collection = "tab_banner")
public class Banner implements Serializable
{
    @Id
    private String id;

    @Field(value = "name")
    private String name;

    @Field(value = "summary")
    private String summary;

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

    @Field(value = "position")
    private int position;

    @Field(value = "image_url")
    private String imageUrl;

    @Field(value = "enable")
    private boolean enable;

    @Field(value = "action_url")
    private String actionUrl;

    @Field(value = "start_time")
    private Date startTime;

    @Field(value = "end_time")
    private Date endTime;

    @Field(value = "deep_link")
    private DeepLink deepLink;

    @Field(value = "payload")
    private String payload;

    public Banner()
    {
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }
}
