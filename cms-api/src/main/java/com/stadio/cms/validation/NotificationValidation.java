package com.stadio.cms.validation;

import com.stadio.common.utils.ResponseCode;
import com.stadio.cms.response.ResponseResult;
import com.stadio.common.utils.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Created by Andy on 02/16/2018.
 */
@Component
public class NotificationValidation extends CustomValidate
{
    public ResponseResult validNotification(String title, String message)
    {

        if (!StringUtils.isNotNull(title))
        {
            return ResponseResult.newInstance(ResponseCode.FAIL, getMessage("notification.invalid.title"), null);
        }

        if (!StringUtils.isNotNull(message))
        {
            return ResponseResult.newInstance(ResponseCode.FAIL, getMessage("notification.invalid.message"), null);
        }

        return null;
    }
}
