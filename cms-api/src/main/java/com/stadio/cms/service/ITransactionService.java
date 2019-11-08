package com.stadio.cms.service;

import com.stadio.cms.response.ResponseResult;
import com.stadio.model.dtos.cms.TransactionFormDTO;
import com.stadio.model.dtos.cms.transaction.TransactionApproveFromSearchDTO;

/**
 * Created by Andy on 03/03/2018.
 */
public interface ITransactionService
{
    ResponseResult processSearch(TransactionFormDTO transactionFormDTO,int page, int pagesize);

    ResponseResult processGetListTransaction(String from, String to);

    ResponseResult processGetListSms(String from, String to);

    ResponseResult processGetListCards(String from, String to);

    ResponseResult groupTransactionByDay(String time);

    ResponseResult groupTransactionByMonth(String time);

    ResponseResult groupTransactionByYear();

    ResponseResult processGetTransactionApproveList(TransactionApproveFromSearchDTO searchFormDTO);

    ResponseResult processUpdateTransactionApproveList(String id, int status);
}
