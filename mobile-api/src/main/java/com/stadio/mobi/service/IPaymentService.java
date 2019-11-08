package com.stadio.mobi.service;

import com.stadio.mobi.response.ResponseResult;

/**
 * Created by Andy on 03/02/2018.
 */
public interface IPaymentService
{
    ResponseResult processPayment(String accessToken, String pin, String serial, String type, String deviceId, String signature);

    String requestOnePay(String pin, String serial, String transRef, String type);

    ResponseResult processGetPublicKeyByDevice(String deviceId);

    ResponseResult processCatchDelayFromOnePay(String amount, String type, String requestTime, String serial, String status, String transRef, String transId);
}
