package com.stadio.model.dtos.cms.recharge;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.RechargeCard;
import lombok.Data;

import java.util.Date;

@Data
public class RechargeCardDTO {

    private String id;

    private String cardNumber;

    private String serial;

    private long value;

    private boolean isUsed;

    private String usedBy;

    private boolean isPrinted;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "GMT+7")
    private Date createdDate;

    public RechargeCardDTO(RechargeCard rechargeCard) {
        this.id = rechargeCard.getId();
        this.cardNumber = rechargeCard.getCardNumber();
        this.serial = rechargeCard.getSerial();
        this.value = rechargeCard.getValue();
        this.isUsed = StringUtils.isNotNull(rechargeCard.getUserIdUsed());
        this.usedBy = rechargeCard.getUserIdUsed();
        this.isPrinted = rechargeCard.isPrinted();

        this.createdDate = rechargeCard.getCreatedDate();
    }
}
