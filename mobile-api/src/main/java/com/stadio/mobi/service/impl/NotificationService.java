package com.stadio.mobi.service.impl;

import com.stadio.common.define.Constant;
import com.stadio.common.enu.Os;
import com.stadio.common.utils.ResponseCode;
import com.stadio.common.utils.StringUtils;
import com.stadio.mobi.RabbitProducer;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.INotificationService;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;

import com.hoc68.users.documents.User;
import com.stadio.model.dtos.cms.NotificationQueue;
import com.stadio.model.repository.main.DeviceRepository;
import com.stadio.model.repository.main.NotificationRepository;
import org.apache.commons.lang3.EnumUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NotificationService extends BaseService implements INotificationService
{

    private Logger logger = LogManager.getLogger(NotificationService.class);

    @Autowired DeviceRepository deviceRepository;

    @Autowired NotificationRepository notificationRepository;

    @Autowired
    RabbitProducer rabbitProducer;

    @Override
    public ResponseResult processRegisterDevice(String accessToken, String deviceModel, String os, String osVersion, String deviceToken, String deviceID)
    {

        if (!EnumUtils.isValidEnum(Os.class, os))
        {
            return ResponseResult.newInstance(ResponseCode.FAIL, getMessage("notification.invalid.os"), null);
        }

        if (!StringUtils.isNotNull(deviceID))
        {
            return ResponseResult.newInstance(ResponseCode.FAIL, getMessage("notification.invalid.deviceID"), null);
        }

        if (!StringUtils.isNotNull(deviceToken))
        {
            return ResponseResult.newInstance(ResponseCode.FAIL, getMessage("notification.invalid.deviceToken"), null);
        }

        Device device = deviceRepository.findOne(deviceID);

        if (device == null)
        {
            device = new Device();
            device.setId(deviceID);
            device.setDeviceId(deviceID);
        }

        if (StringUtils.isNotNull(accessToken))
        {
            String userId = getUserRequesting().getId();
            device.setUserId(userId);

            try {
                List<Device> deviceList = deviceRepository.findDeviceByUserId(userId);
                for (Device oldDevice: deviceList) {
                    oldDevice.setUserId(null);
                    deviceRepository.save(oldDevice);
                }
            } catch (Exception e) {
                logger.error("Update Notification fail: ", e);
            }
        }
        else
        {
            device.setUserId(null);
        }

        device.setUpdatedDate(new Date());
        device.setDeviceModel(deviceModel);
        device.setDeviceOs(Os.valueOf(os));
        device.setOsVersion(osVersion);
        device.setDeviceToken(deviceToken);

        deviceRepository.save(device);

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("notification.success.register.token"), device);
    }

    @Override
    public ResponseResult processGetListWithLastSyncTime(long lastSyncTime) {
        if (lastSyncTime == 0)
        {
            lastSyncTime = System.currentTimeMillis();
        }
        Date sendTime = new Date(lastSyncTime);
        List<Notification> notificationList = notificationRepository.findBySendTimeAfter(sendTime);
        return ResponseResult.newSuccessInstance(notificationList);
    }

    @Override
    public ResponseResult processListMyNotification(int page, int limit, String accessToken) {
        User user = this.getUserRequesting();
        PageRequest request = new PageRequest(page - 1, limit, new Sort(Sort.Direction.DESC, "created_date"));

        List<Notification> notificationList;
        if (user != null) {
            notificationList = notificationRepository.findBySendToOrSendToNull(user.getId(), request).getContent();
        } else {
            notificationList = notificationRepository.findBySendToIsNull(request).getContent();
        }
        return ResponseResult.newSuccessInstance(notificationList);
    }

    @Override
    public void pushSingleDeviceToQueue(Notification notification, List<Device> deviceList) {
        List<String> deviceListInQueue = new ArrayList<>(Constant.Notification.MAX_DEVICES);

        for (Device device: deviceList) {
            deviceListInQueue.add(device.getDeviceToken());
        }
        try
        {
            NotificationQueue queue = new NotificationQueue();
            queue.setDeviceList(deviceListInQueue);
            queue.setOsVersion(null);
            queue.setNotification(notification);
            String jsonArr = mapper.writeValueAsString(queue);
            rabbitProducer.sendNotification(jsonArr);
        }
        catch (Exception e)
        {
            logger.error("RabbitMQ exception pushSingleDeviceToQueue: ", e);
        }
    }
}
