package com.stadio.mobi.dtos;

import lombok.Data;

/**
 * Created by Andy on 03/03/2018.
 */
@Data
public class SmsModel
{
    private String smsValue;;
    private String smsStructure;
    private String smsPhone;

    public SmsModel(String v, String s, String p)
    {
        this.smsValue = v;
        this.smsStructure = s;
        this.smsPhone = p;
    }
}
