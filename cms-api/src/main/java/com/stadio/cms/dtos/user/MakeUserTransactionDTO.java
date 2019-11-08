package com.stadio.cms.dtos.user;

import com.stadio.model.documents.Transaction;
import com.stadio.model.enu.TransactionType;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class MakeUserTransactionDTO {

    @NotNull
    private String transContent;

    private String userIdRef;

    @NotNull
    private long amount;

    private String userCode;

    private Date createdDate;

    public MakeUserTransactionDTO() {}
}
