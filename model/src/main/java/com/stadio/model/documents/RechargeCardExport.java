package com.stadio.model.documents;

import com.stadio.model.model.BaseModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "tab_recharge_card_export")
public class RechargeCardExport extends BaseModel {

    @Id
    private String id;

    @Field("manager_id")
    @TextIndexed
    private String managerId;

    @Field("manager_username")
    @TextIndexed
    private String managerUsername;

    @Field("file_url")
    private String fileUrl;

    @Field("title")
    @TextIndexed
    private String title;

    @Field("card_value")
    private long cardValue;

    @Field("quantity")
    private int quantity;

    @Field("recharge_card_ids")
    private List<String> rechargeCardIds;

}
