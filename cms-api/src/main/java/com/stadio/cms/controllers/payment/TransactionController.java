package com.stadio.cms.controllers.payment;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.ITransactionService;
import com.stadio.model.dtos.cms.TransactionFormDTO;
import com.stadio.model.dtos.cms.transaction.TransactionApproveFromSearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Andy on 03/03/2018.
 */
@RestController
@RequestMapping(value = "api/transaction", name = "Quản lý Giao dịch")
public class TransactionController extends BaseController {

    @Autowired
    ITransactionService transactionService;

    @RequestMapping(value = "/search", method = RequestMethod.POST, name = "Tìm kiếm giao dịch")
    public ResponseResult search(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "30") Integer pageSize,
            @RequestBody TransactionFormDTO transactionFormDTO) {
        return transactionService.processSearch(transactionFormDTO, page, pageSize);
    }

    @GetMapping(value = "", name = "Danh sach giao dịch")
    public ResponseEntity listTransactionCurrentMonth(
            @RequestParam(value = "from", defaultValue = "", required = false) String from,
            @RequestParam(value = "to", defaultValue = "", required = false) String to) {
        ResponseResult result = transactionService.processGetListTransaction(from, to);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/sms")
    public ResponseEntity listSms(
            @RequestParam(value = "from", defaultValue = "", required = false) String from,
            @RequestParam(value = "to", defaultValue = "", required = false) String to) {
        ResponseResult result = transactionService.processGetListSms(from, to);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/card")
    public ResponseEntity listCards(
            @RequestParam(value = "from", defaultValue = "", required = false) String from,
            @RequestParam(value = "to", defaultValue = "", required = false) String to) {
        ResponseResult result = transactionService.processGetListCards(from, to);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/statistic/group-by-day")
    public ResponseEntity statisticTransactionByDay(
            @RequestParam(value = "time", defaultValue = "", required = false) String time
    ) {
        ResponseResult result = transactionService.groupTransactionByDay(time);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/statistic/group-by-month")
    public ResponseEntity statisticTransactionByMonth(
            @RequestParam(value = "time", defaultValue = "", required = false) String time
    ) {
        ResponseResult result = transactionService.groupTransactionByMonth(time);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/statistic/group-by-year")
    public ResponseEntity statisticTransactionByYear(
    ) {
        ResponseResult result = transactionService.groupTransactionByYear();
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/approve/list", name = "Danh sách yêu cầu nạp")
    public ResponseEntity getApproveListTransactions(@RequestBody TransactionApproveFromSearchDTO formDTO
    ) {

        ResponseResult result = transactionService.processGetTransactionApproveList(formDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/approve/update", name = "Phê duyệt/ từ chối yêu cầu nạp")
    public ResponseEntity getApproveListTransactions(
            @RequestParam("id") String id,
            @RequestParam("status") int status
    ) {

        ResponseResult result = transactionService.processUpdateTransactionApproveList(id, status);
        return ResponseEntity.ok(result);
    }

}
