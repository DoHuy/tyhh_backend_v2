package com.stadio.cms.service.impl;

import com.stadio.cms.dtos.PushMessageDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IChatService;
import com.stadio.cms.service.INotificationService;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Device;
import com.stadio.model.documents.Message;
import com.stadio.model.documents.Notification;
import com.hoc68.users.documents.User;
import com.stadio.model.enu.MobileScreen;
import com.stadio.model.enu.NotificationStatus;
import com.stadio.model.enu.NotificationType;
import com.stadio.model.repository.main.DeviceRepository;
import com.stadio.model.repository.main.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andy on 03/04/2018.
 */
@Service
public class ChatService extends BaseService implements IChatService
{

    @Autowired MessageRepository messageRepository;

    @Autowired INotificationService notificationService;

    @Autowired DeviceRepository deviceRepository;

    @Override
    public ResponseResult processPushMessageNow(PushMessageDTO pushMessageDTO)
    {
        if (!StringUtils.isValid(pushMessageDTO.getUsername()))
        {
            return ResponseResult.newErrorInstance("01", this.getMessage("message.empty.username"));
        }

        User user = userRepository.findOneByUsername(pushMessageDTO.getUsername());
        if (user == null)
        {
            return ResponseResult.newErrorInstance("01", this.getMessage("message.notfound.username"));
        }

        Message message = new Message();
        message.setTitle(pushMessageDTO.getTitle());
        message.setContent(pushMessageDTO.getContent());
        message.setUserId(user.getId());

        messageRepository.save(message);

        Notification notification = new Notification();
        notification.setMessage(pushMessageDTO.getContent());
        notification.setTitle(pushMessageDTO.getTitle());

        notification.setScreen(MobileScreen.MESSAGE);
        notification.setStatus(NotificationStatus.PROCESS);

        Date sendTime = new Date(System.currentTimeMillis());
        notification.setSendTime(sendTime);

        notification.setType(NotificationType.FIRE_BASE);

        List<Device> deviceList = deviceRepository.findDeviceByUserId(user.getId());
        notificationService.pushSingleDeviceToQueue(notification, deviceList);

        Map<String, String> body = new HashMap<>();
        body.put("id", message.getId());

        return ResponseResult.newSuccessInstance(body);
    }
}
