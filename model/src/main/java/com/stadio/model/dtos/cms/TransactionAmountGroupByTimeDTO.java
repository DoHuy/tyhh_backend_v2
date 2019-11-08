package com.stadio.model.dtos.cms;

import lombok.Data;

@Data
public class TransactionAmountGroupByTimeDTO {
    private Integer position;// is day , month or year
    private Long amount;

    public TransactionAmountGroupByTimeDTO(Integer position, Long amount) {
        this.position = position;
        this.amount = amount;
    }
}
