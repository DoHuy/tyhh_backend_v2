package com.stadio.mobi.service.impl;

import com.stadio.common.utils.StringUtils;
import com.stadio.mobi.dtos.PushMessageDTO;
import com.stadio.mobi.service.IChatService;
import com.stadio.mobi.service.ITransactionService;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.hoc68.users.documents.User;
import com.stadio.model.enu.TransactionType;
import com.stadio.model.repository.main.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService extends BaseService implements ITransactionService
{
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    IChatService chatService;

    @Override
    public Transaction processRecharge(String token, int amount, TransactionType type)
    {
        if (amount < 0)
        {
            return null;
        }
        User user = this.getUserRequesting();
        if (user != null)
        {
            Transaction transaction = new Transaction();
            transaction.setUserIdRef(user.getId());
            transaction.setAmount(amount);
            transaction.setTransType(type);
            //transactionRepository.save(transaction);

            user.setBalance(user.getBalance() + amount);
            userRepository.save(user);

            PushMessageDTO pushMessageDTO = new PushMessageDTO();

            pushMessageDTO.setUserId(user.getId());
            pushMessageDTO.setContent("Bạn vừa nạp thành công " + StringUtils.separatorNumberWithCharacter(amount) + " đồng vào tài khoản. Số dư " + StringUtils.separatorNumberWithCharacter(user.getBalance()) + " đồng.");
            pushMessageDTO.setTitle("Số dư thay đổi");
            chatService.processPushMessageNow(pushMessageDTO);

            return transaction;
        }

        return null;
    }

    @Override
    public Transaction processRecharge(User user, int amount, TransactionType type)
    {

        if (amount < 0)
        {
            return null;
        }
        Transaction transaction = new Transaction();
        transaction.setUserIdRef(user.getId());
        transaction.setTransContent("Bạn vừa nạp thành công " + StringUtils.separatorNumberWithCharacter(amount) + " đồng vào tài khoản.");
        transaction.setAmount(amount);
        transaction.setTransType(type);
        transactionRepository.createNew(transaction);

        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);

        PushMessageDTO pushMessageDTO = new PushMessageDTO();

        pushMessageDTO.setUserId(user.getId());
        pushMessageDTO.setContent("Bạn vừa nạp thành công " + StringUtils.separatorNumberWithCharacter(amount) + " đồng vào tài khoản. Số dư " + StringUtils.separatorNumberWithCharacter(user.getBalance()) + " đồng.");
        pushMessageDTO.setTitle("Số dư thay đổi");
        chatService.processPushMessageNow(pushMessageDTO);

        return transaction;
    }

    @Override
    public Transaction processDeduction(String token, int offset, TransactionType type)
    {
        if (offset < 0)
        {
            return null;
        }
        User user = this.getUserRequesting();

        if (user != null && user.getBalance() >= offset)
        {
            user.setBalance(user.getBalance() - offset);
            userRepository.save(user);

            Transaction transaction = new Transaction();
            transaction.setAmount((-1) * offset);
            transaction.setTransType(type);
            transaction.setUserIdRef(user.getId());
            transactionRepository.createNew(transaction);

            PushMessageDTO pushMessageDTO = new PushMessageDTO();

            pushMessageDTO.setUserId(user.getId());
            pushMessageDTO.setContent("Bạn vừa mua 1 lần luyện nhanh với " + StringUtils.separatorNumberWithCharacter(offset) + " đồng. Số dư " + StringUtils.separatorNumberWithCharacter(user.getBalance()) + " đồng.");
            pushMessageDTO.setTitle("Số dư thay đổi");
            chatService.processPushMessageNow(pushMessageDTO);

            return transaction;
        }

        return null;
    }

    @Override
    public Transaction processDeduction(String token, int offset, Transaction transaction)
    {
        if (offset < 0 && transaction == null)
        {
            return null;
        }
        User user = this.getUserRequesting();

        if (user != null && user.getBalance() >= offset)
        {
            user.setBalance(user.getBalance() - offset);
            userRepository.save(user);

            transaction.setAmount((-1) * offset);
            transactionRepository.createNew(transaction);

            PushMessageDTO pushMessageDTO = new PushMessageDTO();

            pushMessageDTO.setUserId(user.getId());
            if (transaction.getTransType() == TransactionType.EXAM) {
                pushMessageDTO.setContent("Bạn vừa mua đề thi với " + StringUtils.separatorNumberWithCharacter(offset) + " đồng. Số dư " + StringUtils.separatorNumberWithCharacter(user.getBalance()) + " đồng.");
            } else if (transaction.getTransType() == TransactionType.COURSE) {
                pushMessageDTO.setContent("Bạn vừa mua khoá học với giá " + StringUtils.separatorNumberWithCharacter(offset) + " đồng. Số dư " + StringUtils.separatorNumberWithCharacter(user.getBalance()) + " đồng.");
            } else if (transaction.getTransType() == TransactionType.CATEGORY) {
                pushMessageDTO.setContent("Bạn vừa mua bộ đề với giá " + StringUtils.separatorNumberWithCharacter(offset) + " đồng. Số dư " + StringUtils.separatorNumberWithCharacter(user.getBalance()) + " đồng.");
            }
            pushMessageDTO.setTitle("Số dư thay đổi");
            chatService.processPushMessageNow(pushMessageDTO);

            return transaction;
        }
        return null;
    }
}
