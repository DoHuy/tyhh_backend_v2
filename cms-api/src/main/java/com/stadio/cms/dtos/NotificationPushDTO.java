package com.stadio.cms.dtos;

import lombok.Data;

/**
 * Created by Andy on 02/25/2018.
 */
@Data
public class NotificationPushDTO
{
    private String id;
    private String title;
    private String message;
    private String screen;
    private String type;
    private long sendTime;
    private String userCode;
    private String examCode;

}
