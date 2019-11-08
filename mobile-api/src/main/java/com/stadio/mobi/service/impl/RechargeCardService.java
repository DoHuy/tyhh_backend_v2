package com.stadio.mobi.service.impl;

import com.hoc68.users.documents.User;
import com.stadio.common.define.Constant;
import com.stadio.common.utils.ResponseCode;
import com.stadio.common.utils.StringUtils;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IRechargeCardService;
import com.stadio.mobi.service.IUserService;
import com.stadio.model.documents.CODOrder;
import com.stadio.model.documents.RechargeCard;
import com.stadio.model.documents.UserRechargeAction;
import com.stadio.model.dtos.cms.recharge.CODOrderDTO;
import com.stadio.model.dtos.mobility.recharge.CODOrderFormDTO;
import com.stadio.model.dtos.mobility.recharge.RechargeCardDTO;
import com.stadio.model.enu.CODOrderStatus;
import com.stadio.model.enu.TransactionType;
import com.stadio.model.repository.main.CODOrderRepository;
import com.stadio.model.repository.main.RechargeCardRepository;
import com.stadio.model.repository.main.SequenceIndexRepository;
import com.stadio.model.repository.main.UserRechargeActionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RechargeCardService extends BaseService  implements IRechargeCardService {

    public static final Random gen = new Random();

    @Autowired
    RechargeCardRepository rechargeCardRepository;

    @Autowired
    SequenceIndexRepository sequenceIndexRepository;

    @Autowired
    UserRechargeActionRepository userRechargeActionRepository;

    @Autowired
    CODOrderRepository codOrderRepository;

    @Autowired
    IUserService userService;

    @Autowired
    TransactionService transactionService;

    private Logger logger = LogManager.getLogger(RechargeCardService.class);


    @Override
    public ResponseResult recharge(String cardNumber) {

        UserRechargeAction action = new UserRechargeAction();
        action.setCardNumber(cardNumber);
        action.setUserId(this.getUserRequesting().getId());

        User user = this.getUserRequesting();
        action.setUserId(user.getId());
        action.setUserCode(user.getCode());

        long countLastRechargeFailureIn24H = userRechargeActionRepository.countLastRechargeFailureIn24H(user.getId());
        if (countLastRechargeFailureIn24H >= Constant.MAX_RECHARGE_TIME_PER_DAY && countLastRechargeFailureIn24H < Constant.MAX_RECHARGE_DDOS_TIME_PER_DAY -1) {
            userRechargeActionRepository.save(action);
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("recharge.max.time.action"));
        } else if (countLastRechargeFailureIn24H >= Constant.MAX_RECHARGE_DDOS_TIME_PER_DAY - 1) {
            //Block user by ddos out server

            action.setShouldLockUser(true);
            userRechargeActionRepository.save(action);
            userService.processLockUser();
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("recharge.block.user.action"));
        }

        RechargeCard rechargeCard = rechargeCardRepository.findFirstByCardNumberAndIsEnableIsTrue(cardNumber);
        if (rechargeCard == null || StringUtils.isNotNull(rechargeCard.getUserIdUsed()) || rechargeCard.getValue() == 0) {
            // If reach max rechage time per day
            userRechargeActionRepository.save(action);
            if (countLastRechargeFailureIn24H == Constant.MAX_RECHARGE_TIME_PER_DAY - 1) {
                return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("recharge.max.time.action"));
            }
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("recharge.invalid.card.number") + String.valueOf(Constant.MAX_RECHARGE_TIME_PER_DAY - countLastRechargeFailureIn24H - 1));
        } else {
            if (StringUtils.isNotNull(rechargeCard.getUserIdOrdered()) && !user.getId().equals(rechargeCard.getUserIdOrdered())) {
                userRechargeActionRepository.save(action);
                //If Recharge card was shipped by COD order but different user use this
                return ResponseResult.newErrorInstance(ResponseCode.CONTACT_SUPPORTER, getMessage("recharge.invalid.user.order"));
            }
            rechargeCard.setUserIdUsed(user.getId());
            rechargeCard.setUserCodeUsed(user.getCode());

            CODOrder order = codOrderRepository.findFirstByRechargeCardId(rechargeCard.getId());
            if (order != null) {
                order.setStatus(CODOrderStatus.SUCCESS);
                codOrderRepository.save(order);
            }

            action.setSerial(rechargeCard.getSerial());
            action.setSuccess(true);
            userRechargeActionRepository.save(action);
            rechargeCardRepository.save(rechargeCard);

            transactionService.processRecharge(user, (int) rechargeCard.getValue(), TransactionType.RECHARGE_CARD);
        }

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("recharge.rechage.success") + String.format("%,d", rechargeCard.getValue()), null);
    }

    @Override
    public ResponseResult processCheckCardBySerial(String serial) {
        RechargeCard rechargeCard = rechargeCardRepository.findFirstBySerialAndIsEnable(serial, Boolean.TRUE);
        if (rechargeCard == null || rechargeCard.getValue() == 0) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("recharge.no.serial.match"));
        } else if (StringUtils.isNotNull(rechargeCard.getUserIdUsed())) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("recharge.used"));
        }
        return ResponseResult.newSuccessInstance(new RechargeCardDTO(rechargeCard));
    }

    @Override
    public ResponseResult processCreateCODOrder(CODOrderFormDTO form) {

        long countOrderIn24h = codOrderRepository.countByUserIdAndCreatedDateGreaterThan(getUserRequesting().getId(), new Date(System.currentTimeMillis() - 1L * 24 * 3600 * 1000));

        if (countOrderIn24h >= 10) {
            return ResponseResult.newErrorDefaultInstance(getMessage("cod.spam.too.much"));
        }

        if (form.getValue() <= 0) {
            return ResponseResult.newErrorDefaultInstance(getMessage("cod.wrong.value"));
        }

        if (!StringUtils.isNotNull(form.getUserPhone())) {
            return ResponseResult.newErrorDefaultInstance(getMessage("cod.wrong.value"));
        }

        if (!StringUtils.isNotNull(form.getAddress())) {
            return ResponseResult.newErrorDefaultInstance(getMessage("cod.address.cannot.empty"));
        }

        CODOrder codOrder = new CODOrder();

        User user = getUserRequesting();

        codOrder.setUserId(user.getId());
        codOrder.setUserCode(user.getCode());

        codOrder.setUserPhone(form.getUserPhone());
        codOrder.setUserFullName(form.getUserFullName());
        codOrder.setAddress(form.getAddress());
        codOrder.setNote(form.getNote());
        codOrder.setValue(form.getValue());

        codOrderRepository.saveNew(codOrder);

        return ResponseResult.newSuccessInstance();
    }

}
