package com.stadio.cms.dtos;

import com.stadio.model.documents.Notification;
import lombok.Data;

import java.util.Date;

/**
 * Created by Andy on 02/26/2018.
 */
@Data
public class NotificationDetailsDTO
{
    private String id;
    private String title;
    private String message;
    private String screen;
    private String type;
    private String status;

    private long sendTime;

    public static NotificationDetailsDTO with(Notification notification)
    {
        NotificationDetailsDTO notificationItemDTO = new NotificationDetailsDTO();

        notificationItemDTO.setId(notification.getId());
        notificationItemDTO.setTitle(notification.getTitle());
        notificationItemDTO.setMessage(notification.getMessage());

        notificationItemDTO.setScreen(notification.getScreen().name());
        notificationItemDTO.setType(notification.getType().name());
        notificationItemDTO.setStatus(notification.getStatus().name());

        notificationItemDTO.setSendTime(notification.getSendTime().getTime());

        return notificationItemDTO;
    }
}
