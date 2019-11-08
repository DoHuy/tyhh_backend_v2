package com.stadio.mobi.controllers;

import com.stadio.mobi.payment.IGooglePay;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/pay")
public class GooglePayController extends BaseController {


    @Autowired IGooglePay googlePay;

    @GetMapping()
    public ResponseEntity base64EncodedPublicKey(@RequestHeader(value = "Authorization") String accessToken) {
        ResponseResult result = googlePay.processGetListProducts(accessToken);
        return ResponseEntity.status(200).body(result);
    }

    @PostMapping()
    public ResponseEntity purchase(
            @RequestHeader(value = "Authorization") String accessToken,
            @RequestBody Purchase purchase) {
        ResponseResult result = googlePay.processVerifyAndCreatePurchase(accessToken, purchase);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value ="/payload/{productId}")
    public ResponseEntity payloadSecure(
            @RequestHeader(value = "Authorization") String accessToken,
            @PathVariable String productId) {
        ResponseResult result = googlePay.generatePayload(accessToken, productId);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/verify")
    public ResponseEntity verify(
            @RequestHeader(value = "Authorization") String accessToken,
            @RequestParam(value = "payload") String payload) {
        ResponseResult result = googlePay.verifyPayload(accessToken, payload);
        return ResponseEntity.ok(result);
    }

}
