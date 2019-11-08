package com.stadio.model.dtos.mobility.recharge;

import lombok.Data;

@Data
public class RechargeCardStatusDTO {

    private long totalOfCards;

    private long numberOfBlankCard;

    private long numberOfValuableCard;

    private long numberOfUsedCard;

    private long numberOfPrintedCard;

    private long revenue;

}
