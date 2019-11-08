package com.stadio.model.dtos.cms;

import com.stadio.model.documents.Notification;
import lombok.Data;

import java.util.List;

/**
 * Created by Andy on 02/26/2018.
 */
@Data
public class NotificationQueue
{
    private Notification notification;
    private List<String> deviceList;
    private String osVersion;
}
