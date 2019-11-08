package com.stadio.model.documents;

import com.stadio.model.enu.TransactionType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Andy on 11/10/2017.
 */
@Data
@Document(collection = "tab_transaction")
public class Transaction implements Serializable
{
    @Id
    private String id;

    @Field(value = "trans_no")
    private String transNo;

    @Field(value = "trans_order_in_day")
    private int transOrderInDay;

    @Field(value = "trans_content")
    private String transContent;

    @Field(value = "trans_type")
    private TransactionType transType;

    @Field(value = "trans_type_int")
    private Integer transTypeInt;

    @Field(value = "user_id_ref")
    @Indexed
    private String userIdRef;

    @Field(value = "amount")
    private long amount;

    @Field(value = "object_id")
    private String objectId;

    @Field(value = "created_time")
    @Indexed(name = "idx_created_date", direction = IndexDirection.DESCENDING)
    private Date createdDate;

    public Transaction() {
        this.createdDate = new Date();
    }
}
