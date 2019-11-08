package com.stadio.model.documents;

import com.stadio.common.enu.Os;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "tab_device")
public class Device {
    @Id
    private String id;

    @Field(value = "device_id")
    private String deviceId;

    @Field(value = "device_os")
    private Os deviceOs;

    @Field(value = "os_version")
    private String osVersion;

    @Field(value = "device_model")
    private String deviceModel;

    @Field(value = "device_token")
    private String deviceToken;

    @Field(value = "user_id")
    private String userId;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "updated_date")
    private Date updatedDate;

    public Device(){
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }
}
