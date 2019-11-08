package com.stadio.model.dtos.cms;

import lombok.Data;

import java.util.Date;

@Data
public class TransactionFormDTO {
    private String userCode;
    private String fromDate;
    private String toDate;

}
