package com.stadio.mobi.validation;

import com.stadio.common.utils.StringUtils;
import com.stadio.mobi.response.ResponseResult;
import org.springframework.stereotype.Component;

/**
 * Created by Andy on 02/13/2018.
 */
@Component
public class UserValidation extends CustomValidate
{
    public ResponseResult invalidFiled(String phone, String email)
    {
        if (!StringUtils.isNotNull(email) && !StringUtils.isNotNull(phone))
        {
            return ResponseResult.newErrorInstance("13", getMessage("user.invalid.field"));
        }
        else if (StringUtils.isNotNull(email) && StringUtils.isNotNull(phone))
        {
            return ResponseResult.newErrorInstance("13", getMessage("user.register.invalid.fields"));
        }
        else if (StringUtils.isNotNull(phone) && !StringUtils.isPhoneNumber(phone))
        {
            return ResponseResult.newErrorInstance("13", getMessage("user.invalid.phone"));
        }
        else if (StringUtils.isNotNull(email) && !StringUtils.isValidEmailAddress(email))
        {
            return ResponseResult.newErrorInstance("13", getMessage("user.invalid.email"));
        }

        return null;
    }

}
