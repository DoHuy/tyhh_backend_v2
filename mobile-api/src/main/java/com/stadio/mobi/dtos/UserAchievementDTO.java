package com.stadio.mobi.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class UserAchievementDTO {

    private String id;
    private AchievementItemDTO achievement;
    private Date createdDate;

}
