package com.stadio.cms.dtos;

import lombok.Data;

/**
 * Created by Andy on 03/02/2018.
 */
@Data
public class UserRankDTO
{
    private String id;
    private String username;
    private String fullName;
    private Double point;
    private int position;
    private String avatar;
}
