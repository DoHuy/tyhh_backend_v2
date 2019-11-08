package com.stadio.es.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoc68.users.documents.User;
import com.stadio.common.custom.RequestHandler;
import com.stadio.es.i18n.IMessageService;
import com.stadio.model.repository.user.ManagerRepository;
import com.stadio.model.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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
    
}
