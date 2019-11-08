package com.stadio.mobi.response;

import lombok.Data;

/**
 * Created by Andy on 03/03/2018.
 */
@Data
public class OnePayResponse
{
    private int status;
    private String sms;
    private String type;

    public OnePayResponse(boolean status, String sms, String type) {
        this.status = (status) ? 1 : 0;
        this.sms = sms;
        this.type = type;
    }

    public static OnePayResponse with(boolean status, String sms, String type) {
        return new OnePayResponse(status, sms, type);
    }

    public OnePayResponse() {}
}
