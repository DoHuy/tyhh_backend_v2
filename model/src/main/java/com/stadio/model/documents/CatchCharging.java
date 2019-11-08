package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.lang.annotation.Documented;
import java.util.Date;

/**
 * Created by Andy on 03/03/2018.
 */
@Data
@Document(collection = "1pay_catch_charging")
public class CatchCharging
{
    @Id
    private String id;

    @Field(value = "amount")
    private String amount;

    @Field(value = "type")
    private String type;

    @Field(value = "request_time")
    private String requestTime;

    @Field(value = "serial")
    private String serial;

    @Field(value = "status")
    private String status;

    @Field(value = "trans_ref")
    private String transRef;

    @Field(value = "trans_id")
    private String transId;

    @Field(value = "created_date")
    @Indexed(name = "idx_created_date", direction = IndexDirection.DESCENDING)
    private Date createdDate;

    public CatchCharging()
    {
        this.createdDate = new Date();
    }
}
