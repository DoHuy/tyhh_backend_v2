package com.stadio.cms.service;

import com.stadio.cms.dtos.NotificationPushDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.model.documents.Device;
import com.stadio.model.documents.Notification;
import com.stadio.model.dtos.cms.NotificationSearchDTO;

import java.util.List;

public interface INotificationService {

    ResponseResult processPushNotificationToOneDevice(String to, String title, String message, String action);

    ResponseResult processPushNotificationToAll(String notificationId);

    ResponseResult processCreateNotification(String title, String message, String action);

    ResponseResult processUpdateNotification(NotificationPushDTO notificationPushDTO);

    ResponseResult processDeleteNotification(String id);

    ResponseResult processGetListNotification(Integer page);

    ResponseResult processSearchNotification(NotificationSearchDTO notificationSearchDTO, Integer page, Integer pageSize, String uri);

    ResponseResult processCancelNotification(String id);

    ResponseResult processGetDetails(String id);

    ResponseResult processPushNotificationByQuery(NotificationPushDTO notificationPushDTO);

    void pushSingleDeviceToQueue(Notification notification, List<Device> deviceList);
}
