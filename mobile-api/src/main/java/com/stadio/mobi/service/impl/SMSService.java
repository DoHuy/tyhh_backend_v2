package com.stadio.mobi.service.impl;

import com.stadio.common.utils.StringUtils;
import com.stadio.mobi.payment.OnePayUtils;
import com.stadio.mobi.payment.ParamsBuilder;
import com.stadio.mobi.response.OnePayResponse;
import com.stadio.mobi.service.ISMSService;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.hoc68.users.documents.User;
import com.stadio.model.enu.TransactionType;
import com.stadio.model.repository.main.SmsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Andy on 03/03/2018.
 */
@Service
public class SMSService extends BaseService implements ISMSService
{
    @Autowired SmsRepository smsRepository;

    @Autowired UserService userService;

    @Autowired TransactionService transactionService;

    private Logger logger = LogManager.getLogger(SMSService.class);

    public String signatureBuilder(String access_key, String amount, String command_code,
                                   String mo_message, String msisdn, String telco)
    {

        ParamsBuilder pb = new ParamsBuilder();
        pb.put("access_key", OnePayUtils.ACCESS_KEY);
        pb.put("amount", amount);
        pb.put("command_code", command_code);
        pb.put("mo_message", mo_message);
        pb.put("msisdn", msisdn);
        pb.put("telco", telco);

        return StringUtils.hmac256(OnePayUtils.SECRET_KEY, pb.toParams());
    }

    @Override
    public boolean isValidRequestSignature(String access_key, String amount, String command_code, String error_code, String error_message, String mo_message, String msisdn, String request_id, String request_time, String signature)
    {
        String signatureBuilder = this.signatureRequestBuilder(
                access_key,
                amount,
                command_code,
                error_code,
                error_message,
                mo_message,
                msisdn,
                request_id,
                request_time);
        return signature.equals(signatureBuilder);
    }

    @Override
    public boolean isValidSignature(String access_key, String amount, String command_code, String mo_message, String msisdn, String telco, String signature)
    {
        String signatureBuilder = this.signatureBuilder(
                access_key,
                amount,
                command_code,
                mo_message,
                msisdn,
                telco);
        return signature.equals(signatureBuilder);
    }

    @Override
    public OnePayResponse smsRequest(String accessKey, String amount, String commandCode, String errorCode, String errorMessage, String moMessage, String msisdn, String requestId, String requestTime, String signature)
    {
        OnePayResponse response = new OnePayResponse();
        if (this.isValidRequestSignature(accessKey, amount, commandCode, errorCode, errorMessage, moMessage, msisdn, requestId, requestTime, signature))
        {

            Sms sms = new Sms();
            sms.setAmount(amount);
            sms.setErrorCode(errorCode);
            sms.setErrorMessage(errorMessage);
            sms.setMoMessage(moMessage);
            sms.setMsisdn(msisdn);
            sms.setRequestTime(requestTime);


            try
            {
                String uuid = moMessage.split("[ ]")[2].trim();
                User user = userRepository.findByCode(uuid);
                if (user != null && org.apache.commons.lang3.StringUtils.isNumeric(amount))
                {
                    Transaction transaction = transactionService.processRecharge(user, Integer.parseInt(amount), TransactionType.SMS);
                    sms.setTransactionIdRef(transaction.getId());
                    response.setStatus(1);
                    response.setSms("Giao dich thanh cong, ban vua nap " + amount + " VND vao TK: " + uuid + ", TYHH cam on quy khach da su dung dich vu.");
                }
                else
                {
                    response.setStatus(0);
                    response.setSms("UUID khong ton tai!");
                }
            }
            catch (Exception xp)
            {
                response.setSms("Cu phap khong chinh xac");
                response.setStatus(0);
                logger.error("smsRequest Parsing Exception: ", xp);
            }

            smsRepository.save(sms);
        }
        else
        {
            response.setSms("Mat khau hoac chu ky khong dung");
            response.setStatus(0);
        }
        response.setType("text");

        return response;
    }

    public String signatureRequestBuilder(String access_key, String amount, String command_code, String error_code,
                                          String error_message, String mo_message, String msisdn, String request_id, String request_time)
    {
        ParamsBuilder pb = new ParamsBuilder();
        pb.put("access_key", OnePayUtils.ACCESS_KEY);
        pb.put("amount", amount);
        pb.put("command_code", command_code);
        pb.put("error_code", error_code);
        pb.put("error_message", error_message);
        pb.put("mo_message", mo_message);
        pb.put("msisdn", msisdn);
        pb.put("request_id", request_id);
        pb.put("request_time", request_time);
        return StringUtils.hmac256(OnePayUtils.SECRET_KEY, pb.toParams());
    }
}
