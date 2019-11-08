package com.stadio.model.documents;

import com.stadio.model.model.BaseModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "tab_user_recharge")
public class UserRechargeAction extends BaseModel {

    @Id
    private String id;

    @Field("user_id")
    @TextIndexed
    private String userId;

    @Field("user_code")
    @TextIndexed
    private String userCode;

    @Field("card_number")
    @TextIndexed
    private String cardNumber;

    @Field("serial")
    @TextIndexed
    private String serial;

    @Field("is_success")
    private boolean isSuccess;

    @Field("should_lock_user")
    private boolean shouldLockUser;

    public UserRechargeAction() {
        super();
    }
}
