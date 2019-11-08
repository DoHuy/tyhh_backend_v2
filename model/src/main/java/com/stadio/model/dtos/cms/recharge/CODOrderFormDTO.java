package com.stadio.model.dtos.cms.recharge;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CODOrderFormDTO {

    @NotNull
    private String id;

    @NotNull
    private String userPhone;

    private String userFullName;

    @NotNull
    private String address;

    private long value;

    private String note;
}
