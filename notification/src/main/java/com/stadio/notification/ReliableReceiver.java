package com.stadio.notification;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stadio.common.enu.NotificationPriority;
import com.stadio.model.dtos.cms.NotificationQueue;
import com.stadio.notification.service.IPushNotificationService;
import com.stadio.notification.type.Receiver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Andy on 02/26/2018.
 */
@Component
public class ReliableReceiver implements Receiver
{

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired IPushNotificationService pushNotificationService;

    private Logger logger = LogManager.getLogger(ReliableReceiver.class);

    public void receiveMessage(String message) throws Exception {
        try {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            NotificationQueue queue = mapper.readValue(message, NotificationQueue.class);
            pushNotificationService.sendPushNotification(queue.getDeviceList(), queue.getNotification(), NotificationPriority.normal);
        } catch (Exception e) {
            logger.error("receiveMessage: ", e);
        }

    }
}
