package com.stadio.model.documents;

import com.stadio.model.enu.TransactionType;
import com.stadio.model.model.BaseModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "tab_transaction_approve")
public class TransactionApprove extends BaseModel {

    @Id
    private String id;

    @Field(value = "trans_content")
    private String transContent;

    @Field(value = "trans_type_int")
    private Integer transTypeInt;

    @Field(value = "user_code_ref")
    @Indexed
    private String userCodeRef;

    @Field(value = "amount")
    private long amount;

    @Field(value = "status")
    private int status;

    public TransactionApprove() {
        super();
    }

}
