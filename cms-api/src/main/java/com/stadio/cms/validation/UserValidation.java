package com.stadio.cms.validation;

import com.stadio.cms.response.ResponseResult;
import com.stadio.common.utils.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Created by Andy on 02/13/2018.
 */
@Component
public class UserValidation extends CustomValidate
{
    public ResponseResult invalidFiled(String phone, String email)
    {
        if (email == null && phone == null)
        {
            return ResponseResult.newErrorInstance("13", getMessage("user.invalid.field"));
        }
        else if (phone != null && !StringUtils.isPhoneNumber(phone))
        {
            return ResponseResult.newErrorInstance("13", getMessage("user.invalid.phone"));
        }
        else if (email != null && !StringUtils.isValidEmailAddress(email))
        {
            return ResponseResult.newErrorInstance("13", getMessage("user.invalid.email"));
        }

        return null;
    }

}
