package com.stadio.mobi.controllers;


import com.stadio.common.utils.ResponseCode;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IRechargeCardService;
import com.stadio.model.dtos.mobility.recharge.CODOrderFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/rechargeCard")
public class RechargeCardController extends BaseController {

    @Autowired
    IRechargeCardService paymentCardService;

    @GetMapping("checkSerial")
    public ResponseResult check(@RequestParam("serial") String serial) {
        return paymentCardService.processCheckCardBySerial(serial);
    }

    @PostMapping("recharge")
    public ResponseResult recharge(@RequestParam("cardNumber") String cardNumber) {
        return paymentCardService.recharge(cardNumber);
    }


    @PostMapping(value = "/cod/order")
    public ResponseEntity requestCODOrder(
            @Valid @RequestBody CODOrderFormDTO form, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.ok(ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM, null));
        }
        ResponseResult result = paymentCardService.processCreateCODOrder(form);
        return ResponseEntity.ok(result);
    }

}
