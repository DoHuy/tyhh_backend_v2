package com.stadio.mobi.dtos.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserChangePassDTO {

    @NotNull
    private String oldPass;

    @NotNull
    private String newPass;
}
