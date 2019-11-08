package com.stadio.mobi.dtos;

import lombok.Data;

/**
 * Created by Andy on 03/02/2018.
 */
@Data
public class CardCharging
{
    private String transId;
    private String transRef;
    private String serial;
    private String amount;
    private String status;
    private String description;
}
