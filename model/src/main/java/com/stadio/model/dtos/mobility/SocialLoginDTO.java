package com.stadio.model.dtos.mobility;

import lombok.Data;

/**
 * Created by Andy on 01/19/2018.
 */
@Data
public class SocialLoginDTO
{
    private String email;
    private String phone;
    private String facebookId;
    private String googleId;
    private String accessToken;
    private String avatar;
    private String birthDay;
    private String fullName;

}
