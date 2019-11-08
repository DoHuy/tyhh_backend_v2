package com.stadio.model.documents;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.model.enu.CODOrderStatus;
import com.stadio.model.model.BaseModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "tab_cod_order")
public class CODOrder extends BaseModel {

    @Id
    private String id;

    @Field("code")
    @TextIndexed
    private String code;

    @Field("user_id")
    @TextIndexed
    private String userId;

    @Field("user_code")
    @TextIndexed
    private String userCode;

    @Field("user_phone")
    @TextIndexed
    private String userPhone;

    @Field("user_full_name")
    @TextIndexed
    private String userFullName;

    private long value;

    @Field("address")
    @TextIndexed
    private String address;

    @Field("note")
    private String note;

    @Field("status")
    private CODOrderStatus status;

    @Field("update_reason")
    private String updateReason;

    @Field("recharge_card_id")
    @TextIndexed
    private String rechargeCardId;

    public CODOrder() {
        super();
        this.status = CODOrderStatus.NEW;
    }
}
