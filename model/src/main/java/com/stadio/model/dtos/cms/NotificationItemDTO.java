package com.stadio.model.dtos.cms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.model.documents.Notification;
import lombok.Data;

import java.util.Date;

/**
 * Created by Andy on 02/16/2018.
 */
@Data
public class NotificationItemDTO
{
    private String id;
    private String title;
    private String message;
    private String screen;
    private String type;
    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date sendTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date createdDate;

    public static NotificationItemDTO with(Notification notification)
    {
        NotificationItemDTO notificationItemDTO = new NotificationItemDTO();

        notificationItemDTO.setId(notification.getId());
        notificationItemDTO.setTitle(notification.getTitle());
        notificationItemDTO.setMessage(notification.getMessage());

        notificationItemDTO.setScreen(notification.getScreen().name());
        notificationItemDTO.setType(notification.getType().name());
        notificationItemDTO.setStatus(notification.getStatus().name());

        notificationItemDTO.setSendTime(notification.getSendTime());
        notificationItemDTO.setCreatedDate(notification.getCreatedDate());

        return notificationItemDTO;
    }

}
