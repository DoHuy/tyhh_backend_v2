package com.stadio.mobi.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stadio.common.custom.RequestHandler;
import com.stadio.common.utils.StringUtils;
import com.stadio.mobi.i18n.IMessageService;
import com.hoc68.users.documents.User;
import com.stadio.model.repository.user.ManagerRepository;
import com.stadio.model.repository.user.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Andy on 11/13/2017.
 */
public class BaseService
{
    @Autowired
    private IMessageService messageService;

    @Autowired ManagerRepository managerRepository;

    protected ObjectMapper mapper = new ObjectMapper();

    @Autowired UserRepository userRepository;

    @Autowired
    RequestHandler requestHandler;

    @Value("${hoc68.host}")
    protected String host;

    @Value("${hoc68.mobile.port:#{null}}")
    protected String port;

    protected String getMessage(String key)
    {
        try {
            return messageService.getMessage(key);
        } catch (Exception e) {
            return "OK";
        }
    }

    public User getUserRequesting()
    {
        return userRepository.findOne(this.requestHandler.getPrincipal());
    }

    public boolean isUserLogined() {
        return StringUtils.isNotNull(this.requestHandler.getPrincipal());
    }

}
