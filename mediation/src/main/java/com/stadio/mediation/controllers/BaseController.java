package com.stadio.mediation.controllers;

import com.stadio.mediation.i18n.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;

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

    protected String requestURI(HttpServletRequest request)
    {
        return request.getRequestURI();
    }

}
