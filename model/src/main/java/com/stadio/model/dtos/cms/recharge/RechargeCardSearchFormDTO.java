package com.stadio.model.dtos.cms.recharge;

import lombok.Data;

@Data
public class RechargeCardSearchFormDTO {

    private boolean isPrinted;
    private boolean isUsed;
    private long value;
    private long valueFrom;
    private long valueTo;
    private String serial;
    private int page;
    private int pageSize;

}
