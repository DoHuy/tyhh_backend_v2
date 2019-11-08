package com.stadio.model.dtos.mobility.recharge;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CODOrderFormDTO {

    @NotNull
    private String userPhone;

    @NotNull
    private String userFullName;

    private long value;

    @NotNull
    private String address;

    private String note;
}
