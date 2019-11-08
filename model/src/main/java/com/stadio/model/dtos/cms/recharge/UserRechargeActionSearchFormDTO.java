package com.stadio.model.dtos.cms.recharge;

import lombok.Data;

@Data
public class UserRechargeActionSearchFormDTO {
    private int isSuccess;
    private String userCode;
    private String serial;
    private String cardNumber;
    private int page;
    private int pageSize;
}
