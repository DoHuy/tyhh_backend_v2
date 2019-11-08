package com.stadio.model.dtos.mobility.recharge;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.RechargeCard;
import lombok.Data;

import java.util.Date;

@Data
public class RechargeCardDTO {

    private String id;

    private boolean isUsed;

    public RechargeCardDTO(RechargeCard rechargeCard) {
        this.id = rechargeCard.getId();
        this.isUsed = StringUtils.isNotNull(rechargeCard.getUserIdUsed());
    }
}
