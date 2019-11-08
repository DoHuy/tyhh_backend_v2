package com.stadio.mobi.controllers;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Andy on 03/04/2018.
 */
@RestController
@RequestMapping(value = "api/message")
public class MessageController extends BaseController
{
    @Autowired IChatService chatService;

    @GetMapping()
    public ResponseEntity getListMessage(
            @RequestHeader(value = "Authorization", required = false) String token
    )
    {
        ResponseResult result = chatService.processGetListMessage(token);
        return ResponseEntity.ok(result);
    }
}