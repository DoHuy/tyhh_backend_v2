package com.stadio.mobi.service.impl;

import com.stadio.common.utils.StringUtils;
import com.stadio.mobi.dtos.PushMessageDTO;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IChatService;
import com.stadio.mobi.service.INotificationService;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.hoc68.users.documents.User;
import com.stadio.model.enu.MobileScreen;
import com.stadio.model.enu.NotificationStatus;
import com.stadio.model.enu.NotificationType;
import com.stadio.model.repository.main.DeviceRepository;
import com.stadio.model.repository.main.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Andy on 03/04/2018.
 */
@Service
public class ChatService extends BaseService implements IChatService
{

    @Autowired MessageRepository messageRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    INotificationService notificationService;

    @Override
    public ResponseResult processGetListMessage(String token)
    {
        User user = this.getUserRequesting();
        List<Message> messageList = new ArrayList<>();
        if (user != null){
            messageList = messageRepository.findMessagesByUserIdOrderByCreatedDateDesc(user.getId());
        }
        return ResponseResult.newSuccessInstance(messageList);
    }

    @Override
    public ResponseResult processPushMessageNow(PushMessageDTO pushMessageDTO)
    {
        if (!StringUtils.isValid(pushMessageDTO.getUserId()))
        {
            return ResponseResult.newErrorInstance("01", this.getMessage("message.notfound.username"));
        }

        Message message = new Message();
        message.setTitle(pushMessageDTO.getTitle());
        message.setContent(pushMessageDTO.getContent());
        message.setUserId(pushMessageDTO.getUserId());

        messageRepository.save(message);

        Notification notification = new Notification();
        notification.setMessage(pushMessageDTO.getContent());
        notification.setTitle(pushMessageDTO.getTitle());

        notification.setScreen(MobileScreen.MESSAGE);
        notification.setStatus(NotificationStatus.PROCESS);

        Date sendTime = new Date(System.currentTimeMillis());
        notification.setSendTime(sendTime);

        notification.setType(NotificationType.FIRE_BASE);

        List<Device> deviceList = deviceRepository.findDeviceByUserId(pushMessageDTO.getUserId());
        notificationService.pushSingleDeviceToQueue(notification, deviceList);

        Map<String, String> body = new HashMap<>();
        body.put("id", message.getId());

        return ResponseResult.newSuccessInstance(body);
    }
}
