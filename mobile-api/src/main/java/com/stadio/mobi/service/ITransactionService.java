package com.stadio.mobi.service;

import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.hoc68.users.documents.User;
import com.stadio.model.enu.TransactionType;

public interface ITransactionService
{
    Transaction processRecharge(String token, int amount, TransactionType type);

    Transaction processDeduction(String token, int offset, TransactionType type);

    Transaction processDeduction(String token, int offset, Transaction transaction);

    Transaction processRecharge(User user, int amount, TransactionType type);
}
