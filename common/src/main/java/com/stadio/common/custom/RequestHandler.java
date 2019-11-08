package com.stadio.common.custom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Component
public class RequestHandler {

    @Autowired
    HttpServletRequest httpServletRequest;

    private static Logger logger = LogManager.getLogger(RequestHandler.class);

    public RequestHandler() { }

    public String getPrincipal() {
        try {
            return ((OAuth2Authentication)SecurityContextHolder.getContext().getAuthentication()).getUserAuthentication().getPrincipal().toString();
        } catch (Exception e) {
//            logger.error("Get User Principal exception: ");
            return "";
        }
    }

    public String getToken() {

        return httpServletRequest.getHeader("Authorization");
    }

}

