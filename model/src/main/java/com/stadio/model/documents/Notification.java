package com.stadio.model.documents;

import com.stadio.model.enu.MobileScreen;
import com.stadio.model.enu.NotificationStatus;
import com.stadio.model.enu.NotificationType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "tab_notification")
public class Notification {
    @Id
    private String id;

    @Field(value = "title")
    private String title;

    @Field(value = "type")
    private NotificationType type; //online, offline

    @Field(value = "screen")
    private MobileScreen screen;

    @Field(value = "status")
    private NotificationStatus status;

    @Field(value = "message")
    private String message;

    @Field(value = "objectId")
    private String objectId;

    @Field(value = "send_time")
    private Date sendTime;

    @Field(value = "send_to")
    private String sendTo;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "updated_date")
    private Date updatedDate;

    public Notification(){
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }
}
