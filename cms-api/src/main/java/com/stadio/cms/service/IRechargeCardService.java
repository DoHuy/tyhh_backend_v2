package com.stadio.cms.service;

import com.stadio.model.dtos.cms.recharge.*;
import com.stadio.cms.response.ResponseResult;
import com.stadio.model.enu.CODOrderStatus;

public interface IRechargeCardService {

    ResponseResult processInitNewCards();

    ResponseResult createCards(long value, int quantity);

    ResponseResult processExportToExcel(long value, int quantity);

    ResponseResult getCardsStatus();

    ResponseResult processCheckCardBySerial(String serial);

    ResponseResult searchCards(RechargeCardSearchFormDTO rechargeCardSearchFormDTO);

    ResponseResult getRechargeCardActionHistory(UserRechargeActionSearchFormDTO searchFormDTO);

    ResponseResult getRechargeCardExportHistory(ExportRechargeSearchFormDTO searchFormDTO);

    ResponseResult processGetListCODOrder(CODOrderSearchFormDTO searchFormDTO);

    ResponseResult processUpdateOrderStatus(String orderId, CODOrderStatus status, String reason);

    ResponseResult processUpdateOrder(CODOrderFormDTO form);

}
