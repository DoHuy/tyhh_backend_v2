package com.stadio.model.dtos.cms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.model.documents.Transaction;
import com.stadio.model.enu.TransactionType;
import lombok.Data;

import java.util.Date;

@Data
public class TransactionItemDTO {

    private String id;

    private String transContent;

    private TransactionType transType;

    private String userCode;

    private long amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date createdDate;

    public TransactionItemDTO(Transaction transaction){
        this.id = transaction.getId();
        this.transContent = transaction.getTransContent();
        this.transType = transaction.getTransType();
        this.amount = transaction.getAmount();
        this.createdDate = transaction.getCreatedDate();
    }

}
