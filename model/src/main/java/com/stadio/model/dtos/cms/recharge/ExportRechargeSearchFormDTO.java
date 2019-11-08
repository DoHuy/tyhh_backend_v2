package com.stadio.model.dtos.cms.recharge;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ExportRechargeSearchFormDTO {

    private String managerUsername;
    private String title;
    private long cardValue;
    private int quantity;
    private Date createDate;

    @NotNull
    private int page;

    @NotNull
    private int pageSize;
}
