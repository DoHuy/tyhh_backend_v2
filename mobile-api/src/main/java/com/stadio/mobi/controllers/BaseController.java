package com.stadio.mobi.controllers;

import com.stadio.mobi.i18n.IMessageService;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Andy on 11/08/2017.
 */
public class BaseController
{

    @Autowired
    private IMessageService messageService;

    /**
     * @param key
     * @return
     */
    protected String getMessage(String key)
    {
        return messageService.getMessage(key);
    }

}
