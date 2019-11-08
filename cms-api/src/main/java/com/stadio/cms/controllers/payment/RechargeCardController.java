package com.stadio.cms.controllers.payment;


import com.stadio.cms.controllers.BaseController;
import com.stadio.common.utils.ResponseCode;
import com.stadio.model.documents.RechargeCardExport;
import com.stadio.model.dtos.cms.recharge.*;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IRechargeCardService;
import com.stadio.model.enu.CODOrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/rechargeCard", name = "Quản lý Thẻ nạp")
public class RechargeCardController extends BaseController {

    @Autowired
    IRechargeCardService paymentCardService;

    @PostMapping(value = "list", name = "Danh sách thẻ nạp")
    public ResponseResult getListCard(@RequestBody RechargeCardSearchFormDTO rechargeCardSearchFormDTO) {
        return this.paymentCardService.searchCards(rechargeCardSearchFormDTO);
    }

    @PostMapping(value = "init", name = "Tạo thẻ trắng")
    public ResponseResult initNewCards() {
        return this.paymentCardService.processInitNewCards();
    }


    @PostMapping(value = "create", name = "Tạo thẻ có giá trị")
    public ResponseResult initNewCards(
            @RequestParam("quantity") int quantity,
            @RequestParam("value") int value
    ) {
        return this.paymentCardService.createCards(value, quantity);
    }

    @PostMapping(value = "export", name = "Xuất thẻ")
    public ResponseResult exportCards(
            @RequestParam("quantity") int quantity,
            @RequestParam("value") int value
    ) {
        return this.paymentCardService.processExportToExcel(value, quantity);
    }

    @PostMapping(value = "export/list", name = "Lịch sử xuất bản")
    public ResponseResult check(@RequestBody ExportRechargeSearchFormDTO form) {
        return paymentCardService.getRechargeCardExportHistory(form);
    }

    @GetMapping(value = "checkSerial", name = "Kiểm tra mã serial")
    public ResponseResult check(@RequestParam("serial") String serial) {
        return paymentCardService.processCheckCardBySerial(serial);
    }

    @GetMapping(value = "checkStatus", name = "Thông kê")
    public ResponseResult check() {
        return paymentCardService.getCardsStatus();
    }

    @PostMapping(value = "searchHistory", name = "Lịch sử nạp tiền của người dùng")
    public ResponseResult recharge(@RequestBody UserRechargeActionSearchFormDTO formDTO) {
        return paymentCardService.getRechargeCardActionHistory(formDTO);
    }

    @PostMapping(value = "cod/list", name = "Danh sách phân quyền")
    public ResponseResult getCODOrderList(@RequestBody CODOrderSearchFormDTO form) {
        return paymentCardService.processGetListCODOrder(form);
    }

    @PostMapping(value = "cod/updateStatus", name = "Cập nhập trạng thái COD")
    public ResponseResult getCODOrderList(
            @RequestParam(value = "orderId", required = true) String orderId,
            @RequestParam(value = "status", required = true) CODOrderStatus status,
            @RequestParam(value = "reason", required = false) String reason) {
        return paymentCardService.processUpdateOrderStatus(orderId, status, reason);
    }

    @PostMapping(value = "cod/update", name = "Cập nhập COD")
    public ResponseResult getCODOrderList(
            @Valid @RequestBody CODOrderFormDTO form,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,"");
        }
        return paymentCardService.processUpdateOrder(form);
    }

}
