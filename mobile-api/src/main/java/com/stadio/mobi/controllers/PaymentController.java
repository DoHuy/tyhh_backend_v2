package com.stadio.mobi.controllers;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Andy on 03/02/2018.
 */
@RestController
@RequestMapping(value = "api/payment")
public class PaymentController extends BaseController
{
    @Autowired IPaymentService paymentService;

    @PostMapping()
    public ResponseEntity payment(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(value = "pin", required = false) String pin,
            @RequestParam(value = "serial", required = false) String serial,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "deviceId", required = false) String deviceId,
            @RequestParam(value = "signature", required = false) String signature)
    {
        ResponseResult result = paymentService.processPayment(token, pin, serial, type, deviceId, signature);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/public_key")
    public ResponseEntity getPublicByDevice(
            @RequestParam(value = "deviceId", required = false, defaultValue = "") String deviceId)
    {
        ResponseResult result = paymentService.processGetPublicKeyByDevice(deviceId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/deplay")
    public ResponseEntity catchDelayFromOnePay(
            @RequestParam(value = "amount", required = false) String amount,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "request_time", required = false) String requestTime,
            @RequestParam(value = "serial", required = false) String serial,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "trans_ref", required = false) String transRef,
            @RequestParam(value = "trans_id", required = false) String transId)
    {
        ResponseResult result = paymentService.processCatchDelayFromOnePay(amount, type, requestTime, serial, status, transRef, transId);
        return ResponseEntity.ok(result);
    }

}
