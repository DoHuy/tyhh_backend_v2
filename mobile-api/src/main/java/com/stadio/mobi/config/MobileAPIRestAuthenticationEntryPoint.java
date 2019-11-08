package com.stadio.mobi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stadio.common.utils.ResponseCode;
import com.stadio.mobi.i18n.IMessageService;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.common.custom.RestAuthenticationEntryPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MobileAPIRestAuthenticationEntryPoint extends RestAuthenticationEntryPoint {

    @Autowired
    private IMessageService messageService;

    private Logger logger = LogManager.getLogger(MobileAPIRestAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, org.springframework.security.core.AuthenticationException e) throws IOException, ServletException {

        String message = "login.token.invaild";
        try {
            message = messageService.getMessage(message);
        } catch (Exception exp) {
            logger.error("commence exception: ", e);
        }
        ResponseResult responseResult = ResponseResult.newErrorInstance(ResponseCode.AUTHENTICATE_FAIL, message);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(responseResult);
        super.commence(httpServletRequest,httpServletResponse,e,jsonInString);
    }
}