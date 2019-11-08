package com.stadio.cms.validation;

import com.stadio.cms.i18n.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Andy on 01/31/2018.
 */
public class CustomValidate
{
    @Autowired
    private IMessageService messageService;

    protected String getMessage(String key)
    {
        try {
            return messageService.getMessage(key);
        } catch (Exception e) {
            return "OK";
        }
    }
}
