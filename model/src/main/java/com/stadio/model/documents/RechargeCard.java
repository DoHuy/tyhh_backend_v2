package com.stadio.model.documents;

import com.stadio.model.model.BaseModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "tab_recharge_card")
public class RechargeCard extends BaseModel{

    @Id
    private String id;

    @Field("card_number")
    @TextIndexed
    private String cardNumber;

    @Field("card_number_suffix")
    @Indexed
    private int cardNumberSuffix;

    @Field("serial")
    @TextIndexed
    private String serial;

    @Field("user_id_used")
    private String userIdUsed;

    @Field("user_code_used")
    private String userCodeUsed;

    @Field("user_id_ordered")
    @TextIndexed
    private String userIdOrdered;

    @Field("is_printed")
    private boolean isPrinted;

    @Field("value")
    @Indexed
    private long value;

    @Field("is_enable")
    private Boolean isEnable;

    public RechargeCard() {
        super();
        this.isEnable = Boolean.TRUE;
    }
}
