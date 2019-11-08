package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by Andy on 03/02/2018.
 */
@Data
@Document(collection = "1pay_request")
public class OnePayRequest
{
    @Id
    private String id;

    @Field(value = "user_id_ref")
    private String userIdRef;

    @Field(value = "trans_id")
    private String transId;

    @Field(value = "trans_ref")
    private String transRef;

    @Field(value = "serial")
    private String serial;

    @Field(value = "amount")
    private String amount;

    @Field(value = "status")
    private String status;

    @Field(value = "description")
    private String description;

    @Field(value = "pin")
    private String pin;

    @Field(value = "transaction_id_ref")
    private String transactionIdRef;

    @Field(value = "created_date")
    @Indexed(name = "idx_created_date", direction = IndexDirection.DESCENDING)
    private Date createdDate;

    public OnePayRequest()
    {
        this.createdDate = new Date();
    }
}
