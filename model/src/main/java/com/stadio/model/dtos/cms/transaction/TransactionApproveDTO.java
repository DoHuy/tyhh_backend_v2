package com.stadio.model.dtos.cms.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.model.documents.TransactionApprove;
import com.stadio.model.dtos.cms.ManagerDTO;
import com.stadio.model.enu.TransactionApproveStatus;
import lombok.Data;

import java.util.Date;

@Data
public class TransactionApproveDTO {

    private String id;

    private String transContent;

    private String userCode;

    private String managerIdCreated;

    private String managerUsernameCreated;

    private String managerIdUpdated;

    private String managerUsernameUpdated;

    private long amount;

    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "GMT+7")
    private Date createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "GMT+7")
    private Date updatedDate;

    public TransactionApproveDTO(TransactionApprove transactionApprove) {
        this.id = transactionApprove.getId();
        this.transContent = transactionApprove.getTransContent();
        this.userCode = transactionApprove.getUserCodeRef();
//        this.createdBy = transactionApprove.getCreatedBy();
        this.amount = transactionApprove.getAmount();
        this.status = TransactionApproveStatus.fromInt(transactionApprove.getStatus()).name();
        this.createdDate = transactionApprove.getCreatedDate();
        this.updatedDate = transactionApprove.getUpdatedDate();
    }
}
