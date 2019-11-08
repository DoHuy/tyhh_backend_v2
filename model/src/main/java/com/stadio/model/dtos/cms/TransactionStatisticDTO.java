package com.stadio.model.dtos.cms;

import lombok.Data;

@Data
public class TransactionStatisticDTO {
    private int position;// is day , month or year
    private long amountRecharge;
    private long amountBuy;

    public TransactionStatisticDTO(){
        amountRecharge = 0;
        amountBuy = 0;

    }
}
