package com.stadio.model.repository.main.custom;

import com.stadio.model.documents.Notification;

import java.util.List;
import java.util.Map;

public interface NotificationRepositoryCustom
{

    List<Notification> findNotificationByPage(Integer page);

    List<Notification> searchNotification(Integer page, Integer pageSize, Map<String, String> search);
}
