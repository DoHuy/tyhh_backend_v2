package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by Andy on 03/02/2018.
 */
@Data
@Document(collection = "tab_public_key")
public class PublicKey
{
    @Id
    private String id;

    @Field(value = "public_key")
    private String publicKey;

    @Field(value = "device_id")
    private String deviceId;

    @Field(value = "created_date")
    private Date createdDate;

    public PublicKey()
    {
        this.createdDate = new Date();
    }
}
