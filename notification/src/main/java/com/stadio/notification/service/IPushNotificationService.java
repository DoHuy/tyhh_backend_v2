package com.stadio.notification.service;

import com.hoc68.users.documents.User;
import com.stadio.common.enu.NotificationPriority;
import com.stadio.common.utils.Tuple;
import com.stadio.model.documents.Notification;

import java.util.List;

public interface IPushNotificationService
{
    void sendPushNotification(List<String> deviceToken, Notification notification, NotificationPriority priority) throws Exception;

    void sendPushNotification(List<User> userList, Notification notification);

    void sendPushNotification(String userId, Notification notification);
}
