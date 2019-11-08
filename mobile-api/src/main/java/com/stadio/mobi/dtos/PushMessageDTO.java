package com.stadio.mobi.dtos;

import lombok.Data;

/**
 * Created by Andy on 03/04/2018.
 */
@Data
public class PushMessageDTO
{
    private String userId;
    private String title;
    private String content;
}
