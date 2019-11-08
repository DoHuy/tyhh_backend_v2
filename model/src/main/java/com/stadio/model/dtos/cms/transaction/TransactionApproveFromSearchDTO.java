package com.stadio.model.dtos.cms.transaction;

import lombok.Data;

@Data
public class TransactionApproveFromSearchDTO {

    private int status;
    private String userCode;
    private String createdBy;
    private String transactionContent;
    private long value;
    private int page;
    private int pageSize;

}
