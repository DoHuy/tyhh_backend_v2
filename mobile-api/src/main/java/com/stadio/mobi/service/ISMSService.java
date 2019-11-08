package com.stadio.mobi.service;

import com.stadio.mobi.response.OnePayResponse;

/**
 * Created by Andy on 03/03/2018.
 */
public interface ISMSService
{
    boolean isValidRequestSignature(String access_key, String amount, String command_code, String error_code,
                                            String error_message, String mo_message, String msisdn, String request_id, String request_time, String signature);

    boolean isValidSignature(String access_key, String amount, String command_code,
                                    String mo_message, String msisdn, String telco, String signature);

    OnePayResponse smsRequest(String accessKey, String amount, String commandCode, String errorCode, String errorMessage, String moMessage, String msisdn, String requestId, String requestTime, String signature);
}
