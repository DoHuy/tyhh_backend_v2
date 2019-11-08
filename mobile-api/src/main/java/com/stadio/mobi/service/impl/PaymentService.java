package com.stadio.mobi.service.impl;

import com.hoc68.users.documents.User;
import com.stadio.common.enu.FolderName;
import com.stadio.common.service.impl.StorageService;
import com.stadio.common.utils.StringUtils;
import com.stadio.mobi.dtos.CardCharging;
import com.stadio.mobi.payment.OnePayUtils;
import com.stadio.mobi.payment.ParamsBuilder;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IPaymentService;
import com.stadio.mobi.service.IUserService;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.enu.TransactionType;
import com.stadio.model.repository.main.CatchChargingRepository;
import com.stadio.model.repository.main.OnePayRequestRepository;
import com.stadio.model.repository.main.PublicKeyRepository;
import com.stadio.model.repository.main.TransactionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Andy on 03/02/2018.
 */
@Service
public class PaymentService extends BaseService implements IPaymentService
{

    private Logger logger = LogManager.getLogger(PaymentService.class);

    @Autowired PublicKeyRepository publicKeyRepository;

    @Autowired StorageService storageService;

    @Autowired IUserService userService;

    @Autowired OnePayRequestRepository onePayRequestRepository;

    @Autowired CatchChargingRepository catchChargingRepository;

    @Autowired TransactionService transactionService;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public ResponseResult processPayment(String accessToken, String pin, String serial, String type, String deviceId, String signature)
    {

        User user = this.getUserRequesting();

        ParamsBuilder pb = new ParamsBuilder();
        pb.put("access_token", accessToken);
        pb.put("serial", serial);
        pb.put("pin", pin);
        pb.put("type", type);

        PublicKey publicKey = publicKeyRepository.findByDeviceId(deviceId);

        String ensig = StringUtils.hmac256(publicKey.getPublicKey(), pb.toParams());
        if(!ensig.equals(signature))
        {
            return ResponseResult.newErrorInstance("01","Chữ ký không chính xác");
        }

        if (serial.equals("818533") && (pin.equals("1000") || pin.equals("5000") || pin.equals("10000"))){
            return demoPurchasePhoneCard(accessToken, pin, type);
        }

        String transRef = StringUtils.identifier256();

        String res = this.requestOnePay(pin, serial, transRef, type);
        printFile(res, user.getId());

        ResponseResult response = new ResponseResult();
        try
        {
            CardCharging charging = mapper.readValue(res, CardCharging.class);

            if("00".equals(charging.getStatus()))
            {
                if (org.apache.commons.lang3.StringUtils.isNumeric(charging.getAmount()))
                {
                    int amount = Integer.parseInt(charging.getAmount());
                    Transaction transaction = transactionService.processRecharge(accessToken, amount, TransactionType.CARD);
                    transaction.setTransContent(String.format("Nạp thẻ %s", type));
                    transactionRepository.createNew(transaction);
                    response.setErrorCode(charging.getStatus());
                    response.setMessage(charging.getDescription());
                    saveRequest(charging, transaction.getId(), pin);
                }
                else
                {
                    response.setErrorCode("02");
                    response.setMessage("Lỗi số tiền nạp không hợp lệ");
                    saveRequest(charging, null, pin);
                }

            }
            else
            {
                response.setErrorCode(charging.getStatus());
                response.setMessage(charging.getDescription());
                saveRequest(charging, null, pin);
            }
        }
        catch (Exception e)
        {
            response.setErrorCode("02");
            logger.error("printFile Exception: ", e);
        }

        return response;
    }

    private ResponseResult demoPurchasePhoneCard(String accessToken, String pin, String type){

        if (org.apache.commons.lang3.StringUtils.isNumeric(pin))
        {
            int amount = Integer.parseInt(pin);

            Transaction transaction = transactionService.processRecharge(accessToken, amount, TransactionType.CARD);
            transaction.setTransContent(String.format("Nạp thẻ %s", type));
            transactionRepository.createNew(transaction);
        }

        return ResponseResult.newSuccessInstance(null);
    }

    private void saveRequest(CardCharging charging, String transactionId, String pin)
    {
        OnePayRequest onePayRequest = new OnePayRequest();
        onePayRequest.setAmount(charging.getAmount());
        onePayRequest.setDescription(charging.getDescription());
        onePayRequest.setPin(pin);
        onePayRequest.setStatus(charging.getStatus());
        onePayRequest.setTransactionIdRef(transactionId);
        onePayRequest.setSerial(charging.getSerial());
        onePayRequest.setTransId(charging.getTransId());
        onePayRequest.setTransRef(charging.getTransRef());

        onePayRequestRepository.save(onePayRequest);
    }

    private void printFile(String data, String userId)
    {
        try
        {
            Path p = storageService.getLocation(FolderName.USERS)
                    .resolve("payment")
                    .resolve(userId);

            if (!p.toFile().exists())
            {
                Files.createDirectories(p);
            }
            String fname = System.currentTimeMillis() + ".txt";
            File f = p.resolve(fname).toFile();
            mapper.writeValue(f, data);
        }
        catch (Exception e)
        {
            logger.error("printFile Exception: ", e);
        }

    }

    @Override
    public String requestOnePay(String pin, String serial, String transRef, String type)
    {
        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();

        try
        {
            URL url = new URL(OnePayUtils.REQUEST_CHARGING);

            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);

            String signature = this.signatureBuilder(pin, serial, transRef, type);
            String urlParameters = paramsBuilder(pin, serial, transRef, type, signature);

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(urlParameters);
            writer.flush();
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));


            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
            writer.close();
            reader.close();
        }
        catch (Exception xp)
        {

        }
        finally
        {
            if (conn != null)
            {
                conn.disconnect();
            }
        }

        return sb.toString();
    }

    @Override
    public ResponseResult processGetPublicKeyByDevice(String deviceId)
    {
        PublicKey key = publicKeyRepository.findByDeviceId(deviceId);
        if(key == null)
        {
            key = new PublicKey();
            key.setPublicKey(StringUtils.identifier256());
            key.setDeviceId(deviceId);
            publicKeyRepository.save(key);
        }

        return ResponseResult.newSuccessInstance(key);
    }

    @Override
    public ResponseResult processCatchDelayFromOnePay(String amount, String type, String requestTime, String serial, String status, String transRef, String transId)
    {
        CatchCharging catchCharging = new CatchCharging();
        catchCharging.setAmount(amount);
        catchCharging.setRequestTime(requestTime);
        catchCharging.setType(type);
        catchCharging.setStatus(status);
        catchCharging.setSerial(serial);
        catchCharging.setTransId(transId);
        catchCharging.setTransRef(transRef);

        catchChargingRepository.save(catchCharging);

        return ResponseResult.newSuccessInstance(null);
    }

    public String paramsBuilder(String pin, String serial, String transRef, String type, String signature)
    {

        ParamsBuilder pb = new ParamsBuilder();
        pb.put("access_key", OnePayUtils.ACCESS_KEY);
        pb.put("pin", pin);
        pb.put("serial", serial);
        pb.put("transRef", transRef);
        pb.put("type", type);
        pb.put("signature", signature);

        return pb.toParams();
    }

    public String signatureBuilder(String pin, String serial, String transRef, String type)
    {

        ParamsBuilder pb = new ParamsBuilder();
        pb.put("access_key", OnePayUtils.ACCESS_KEY);
        pb.put("pin", pin);
        pb.put("serial", serial);
        pb.put("transRef", transRef);
        pb.put("type", type);

        return StringUtils.hmac256(OnePayUtils.SECRET_KEY, pb.toParams());
    }
}
