package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "google_order")
@Data
public class GoogleOrder {

    @Id
    private String id;

    @Field(value = "payload")
    private String payload;

    @Field(value = "user_id")
    private String userId;

    @Field(value = "status")
    private int status; //0: valid, chua xu ly || 1: invalid, da xu ly

    @Field(value = "product_id")
    private String productId;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "updated_date")
    private Date updatedDate;

    public GoogleOrder(String userId, String payload, int status, String productId) {
        this.createdDate = new Date();
        this.updatedDate = new Date();
        this.userId = userId;
        this.status = status;
        this.payload = payload;
        this.productId = productId;
    }
}
