package com.stadio.mobi.service;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;

import java.util.List;

public interface INotificationService
{

    ResponseResult processRegisterDevice(String accessToken, String deviceModel, String os, String osVersion, String deviceToken, String deviceID);

    ResponseResult processGetListWithLastSyncTime(long lastSyncTime);

    ResponseResult processListMyNotification(int page, int limit, String accessToken);

    void pushSingleDeviceToQueue(Notification notification, List<Device> deviceList);
}
