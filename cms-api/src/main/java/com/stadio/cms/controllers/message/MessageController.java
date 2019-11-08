package com.stadio.cms.controllers.message;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.dtos.PushMessageDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Andy on 03/04/2018.
 */
@RestController
@RequestMapping(value = "api/message", name = "Quản lý nội dung tin nhắn")
public class MessageController extends BaseController
{
    @Autowired IChatService chatService;

    @PostMapping(value = "/push", name = "Gửi tin nhắn")
    public ResponseEntity pushMessage(
            @RequestBody PushMessageDTO pushMessageDTO)
    {
        ResponseResult result = chatService.processPushMessageNow(pushMessageDTO);
        return ResponseEntity.ok(result);
    }
}
