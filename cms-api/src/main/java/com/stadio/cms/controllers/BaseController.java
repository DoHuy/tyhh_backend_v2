package com.stadio.cms.controllers;

import com.stadio.cms.i18n.IMessageService;
import com.stadio.cms.service.IManagerService;
import com.hoc68.users.documents.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Andy on 11/08/2017.
 */
public class BaseController {

    @Autowired
    public IMessageService messageService;

    @Autowired
    public IManagerService managerService;

    @Value("${hoc68.host}")
    protected String host;

    @Value("${hoc68.mobile.port:#{null}}")
    protected String port;

    /**
     * @param key
     * @return
     */
    protected String getMessage(String key) {
        return messageService.getMessage(key);
    }

    protected String requestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    public Manager getCurrentUserManager() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return managerService.findOneByUsername(currentPrincipalName);
    }
}
