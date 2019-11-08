package com.stadio.mobi.dtos.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserForgotPassDTO {

    @NotNull
    private String accessToken;

    @NotNull
    private String phone;

    @NotNull
    private String newPass;

}
