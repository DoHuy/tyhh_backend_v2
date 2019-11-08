package com.stadio.mobi.payment;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;

public interface IGooglePay {

    ResponseResult processVerifyAndCreatePurchase(String token, Purchase purchase);

    ResponseResult processGetListProducts(String accessToken);

    ResponseResult generatePayload(String token, String productId);

    ResponseResult verifyPayload(String token, String payload);

}
