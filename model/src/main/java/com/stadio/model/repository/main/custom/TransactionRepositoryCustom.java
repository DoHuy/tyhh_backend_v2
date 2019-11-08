package com.stadio.model.repository.main.custom;

import com.mongodb.DBObject;
import com.stadio.model.documents.Transaction;
import com.stadio.model.dtos.cms.TransactionFormDTO;

import java.util.List;

public interface TransactionRepositoryCustom {

    List<Transaction> search(TransactionFormDTO transactionFormDTO,String userId, Integer page, Integer pageSize);

    long countSearch(TransactionFormDTO transactionFormDTO,String userId, Integer page, Integer pageSize);

    List findTransactionsByPage(String userId,Integer page, Integer pageSize);

    void createNew(Transaction transaction);

    Iterable<DBObject> groupTransactionRechargeByDay(int month, int year);

    Iterable<DBObject> groupTransactionBuyByDay(int month, int year);

    Iterable<DBObject> groupTransactionRechargeByMonth(int year);

    Iterable<DBObject> groupTransactionBuyByMonth(int year);

    Iterable<DBObject> groupTransactionRechargeByYear();

    Iterable<DBObject> groupTransactionBuyByYear();

}
