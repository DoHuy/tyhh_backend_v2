package com.stadio.mobi.dtos.user;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class UserUpdateProfileDTO {

    private String fullname;

    private String clazzId;

    @NotNull
    private String phone;

    private String birthday;

    private String email;

    private String address;

    private String idNumber; //so chung minh thu

    private Date idIssueDate; //ngay cap

}
