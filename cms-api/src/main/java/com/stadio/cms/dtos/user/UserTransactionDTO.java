package com.stadio.cms.dtos.user;

import com.stadio.model.documents.Transaction;
import com.stadio.model.enu.TransactionType;
import lombok.Data;

import java.util.Date;

@Data
public class UserTransactionDTO {

    private String id;

    private String transContent;

    private TransactionType transType;

    private String userIdRef;

    private long amount;

    private Date createdDate;

    public UserTransactionDTO() {}

    public UserTransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.transContent = transaction.getTransContent();
        this.transType = transaction.getTransType();
        this.userIdRef = userIdRef;
        this.amount = transaction.getAmount();
        this.createdDate = transaction.getCreatedDate();
    }
}
