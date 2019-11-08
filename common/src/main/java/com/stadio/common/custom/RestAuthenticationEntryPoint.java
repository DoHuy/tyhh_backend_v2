package com.stadio.common.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
    }

    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, org.springframework.security.core.AuthenticationException e, String reponseBody) throws IOException, ServletException {
        httpServletResponse.addHeader("Content-Type", "application/json");
        httpServletResponse.setStatus(401);

        ServletOutputStream out = httpServletResponse.getOutputStream();
        out.write(reponseBody.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}