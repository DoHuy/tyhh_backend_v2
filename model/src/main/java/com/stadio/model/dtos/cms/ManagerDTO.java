package com.stadio.model.dtos.cms;

import lombok.Data;

/**
 * Created by Andy on 11/08/2017.
 */
@Data
public class ManagerDTO
{
    private String id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String fullName;
    private String role; //for older version
    private String address;
}
