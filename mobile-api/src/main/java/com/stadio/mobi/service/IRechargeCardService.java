package com.stadio.mobi.service;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.model.dtos.cms.recharge.RechargeCardSearchFormDTO;
import com.stadio.model.dtos.mobility.recharge.CODOrderFormDTO;

public interface IRechargeCardService {

    ResponseResult recharge(String cardNumber);

    ResponseResult processCheckCardBySerial(String serial);

    ResponseResult processCreateCODOrder(CODOrderFormDTO form);

}
