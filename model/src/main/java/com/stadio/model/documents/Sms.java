package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by Andy on 03/03/2018.
 */
@Data
@Document(collection = "1pay_sms")
public class Sms
{
    @Id
    private String id;

    @Field(value = "amount")
    private String amount;

    @Field(value = "error_code")
    private String errorCode;

    @Field(value = "error_message")
    private String errorMessage;

    @Field(value = "msisdn")
    private String msisdn;

    @Field(value = "mo_message")
    private String moMessage;

    @Field(value = "request_time")
    private String requestTime;

    @Field(value = "transaction_id")
    private String transactionIdRef;

    @Field(value = "created_date")
    @Indexed(name = "idx_created_date", direction = IndexDirection.DESCENDING)
    private Date createdDate;

    public Sms()
    {
        this.createdDate = new Date();
    }
}
