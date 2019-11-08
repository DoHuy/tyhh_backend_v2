package com.stadio.mobi.controllers;

import com.stadio.mobi.dtos.SmsModel;
import com.stadio.mobi.response.OnePayResponse;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.ISMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 03/03/2018.
 */
@RestController
@RequestMapping(value = "api/sms")
public class SMSController extends BaseController
{

    @Autowired ISMSService smsService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity listSms(HttpServletRequest request)
            throws Exception
    {

        List<SmsModel> items = new ArrayList<SmsModel>();
        items.add(new SmsModel("5000", "MW TYHH NAP5", "9029"));
        items.add(new SmsModel("10000", "MW TYHH NAP10", "9029"));
        items.add(new SmsModel("15000", "MW TYHH NAP20", "9029"));

        return  ResponseEntity.ok(ResponseResult.newSuccessInstance(items));
    }


    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public @ResponseBody
    OnePayResponse checkMo(
            @RequestParam(value = "access_key", required = false) String access_key,
            @RequestParam(value = "amount", required = false) String amount,
            @RequestParam(value = "command_code", required = false) String command_code,
            @RequestParam(value = "mo_message", required = false) String mo_message,
            @RequestParam(value = "msisdn", required = false) String msisdn,
            @RequestParam(value = "telco", required = false) String telco,
            @RequestParam(value = "signature", required = false) String signature)
    {

        if (access_key == null)
        {
            return OnePayResponse.with(false, "Tham so 'access_key' khong dung", "text");
        }
        else if (amount == null)
        {
            return OnePayResponse.with(false, "Tham so 'amount' khong dung", "text");
        }
        else if (command_code == null)
        {
            return OnePayResponse.with(false, "Tham so 'command_code' khong dung", "text");
        }
        else if (mo_message == null)
        {
            return OnePayResponse.with(false, "Tham so 'mo_message' khong dung", "text");
        }
        else if (msisdn == null)
        {
            return OnePayResponse.with(false, "Tham so 'msisdn' khong dung", "text");
        }
        else if (telco == null)
        {
            return OnePayResponse.with(false, "Tham so 'telco' khong dung", "text");
        }

        if (smsService.isValidSignature(access_key, amount, command_code, mo_message, msisdn, telco, signature))
        {
            return OnePayResponse.with(true, "Giao dich thanh cong, cam on quy khach da su dung dich vu cua chung toi", "text");
        }
        else
        {
            return OnePayResponse.with(false, "Mat khau hoac chu ky khong dung", "text");
        }
    }

    @RequestMapping(value = "/request", method = RequestMethod.GET)
    public OnePayResponse request(
            @RequestParam(value = "access_key", required = false) String accessKey,
            @RequestParam(value = "amount", required = false) String amount,
            @RequestParam(value = "command_code", required = false) String commandCode,
            @RequestParam(value = "error_code", required = false) String errorCode,
            @RequestParam(value = "error_message", required = false) String errorMessage,
            @RequestParam(value = "mo_message", required = false) String moMessage,
            @RequestParam(value = "msisdn", required = false) String msisdn,
            @RequestParam(value = "request_id", required = false) String requestId,
            @RequestParam(value = "request_time", required = false) String requestTime,
            @RequestParam(value = "signature", required = false) String signature)
    {
        if (accessKey == null)
        {
            return OnePayResponse.with(false, "Tham so 'access_key' khong dung", "text");
        }
        else if (amount == null)
        {
            return OnePayResponse.with(false, "Tham so 'amount' khong dung", "text");
        }
        else if (commandCode == null)
        {
            return OnePayResponse.with(false, "Tham so 'command_code' khong dung", "text");
        }
        else if (moMessage == null)
        {
            return OnePayResponse.with(false, "Tham so 'mo_message' khong dung", "text");
        }
        else if (msisdn == null)
        {
            return OnePayResponse.with(false, "Tham so 'msisdn' khong dung", "text");
        }
        else if (requestId == null)
        {
            return OnePayResponse.with(false, "Tham so 'request_id' khong dung", "text");
        }
        else if (requestTime == null)
        {
            return OnePayResponse.with(false, "Tham so 'request_time' khong dung", "text");
        }
        else if (errorCode == null)
        {
            return OnePayResponse.with(false, "Tham so 'error_code' khong dung", "text");
        }
        else if (errorMessage == null)
        {
            return OnePayResponse.with(false, "Tham so 'error_message' khong dung", "text");
        }

        OnePayResponse response = smsService.smsRequest(accessKey, amount, commandCode, errorCode, errorMessage, moMessage, msisdn, requestId, requestTime, signature);
        return response;
    }
}
