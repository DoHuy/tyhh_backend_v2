package com.stadio.cms.service.impl;

import com.stadio.cms.RabbitProducer;
import com.stadio.cms.dtos.NotificationDetailsDTO;
import com.stadio.cms.dtos.NotificationPushDTO;
import com.stadio.cms.model.PageInfo;
import com.stadio.common.utils.ResponseCode;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.response.TableList;
import com.stadio.cms.service.INotificationService;
import com.stadio.cms.validation.NotificationValidation;
import com.stadio.common.define.Constant;
import com.stadio.common.enu.Os;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Device;
import com.stadio.model.documents.Exam;
import com.stadio.model.documents.ExamOnline;
import com.stadio.model.documents.Notification;
import com.hoc68.users.documents.User;
import com.stadio.model.dtos.cms.NotificationItemDTO;
import com.stadio.model.dtos.cms.NotificationQueue;
import com.stadio.model.dtos.cms.NotificationSearchDTO;
import com.stadio.model.enu.MobileScreen;
import com.stadio.model.enu.NotificationStatus;
import com.stadio.model.enu.NotificationType;
import com.stadio.model.enu.OnlineTestStatus;
import com.stadio.model.redisUtils.MappingRedis;
import com.stadio.model.repository.main.DeviceRepository;
import com.stadio.model.repository.main.ExamOnlineRepository;
import com.stadio.model.repository.main.ExamRepository;
import com.stadio.model.repository.main.NotificationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService extends BaseService implements INotificationService
{

    private Logger logger = LogManager.getLogger(NotificationService.class);

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired NotificationValidation validation;

    @Autowired RabbitProducer rabbitProducer;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    ExamOnlineRepository examOnlineRepository;

    @Override
    public ResponseResult processPushNotificationToOneDevice(String deviceToken, String title, String message, String action)
    {

        ResponseResult res = validation.validNotification(title, message);

        if (res != null)
        {
            return res;
        }

        List<String> tokens = new ArrayList<>();
        tokens.add(deviceToken);

        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setScreen(MobileScreen.HOME);
        try
        {
            //PushNotificationService pushNotificationService = new PushNotificationService();
            //Tuple<Boolean, String> response = pushNotificationService.sendPushNotification(tokens, notification, NotificationPriority.normal);
            return ResponseResult.newSuccessInstance(null);
        }
        catch (Exception e)
        {
            logger.error("PushNotificationOneDevice: ", e);

        }
        return ResponseResult.newInstance(ResponseCode.FAIL, "Push notification exception!", null);
    }

    @Override
    public ResponseResult processPushNotificationToAll(String notificationId)
    {

        Notification notification = notificationRepository.findOne(notificationId);

        if (notification == null)
        {
            return ResponseResult.newInstance(ResponseCode.FAIL, getMessage("notification.invalid.title"), null);
        }

        List<String> tokens = new ArrayList<>();

        for (Device device : deviceRepository.findAll())
        {
            if (StringUtils.isNotNull(device.getDeviceToken()))
            {
                tokens.add(device.getDeviceToken());
            }
        }

        try
        {

            //PushNotificationService pushNotificationService = new PushNotificationService();

            //Tuple<Boolean, String> response = pushNotificationService.sendPushNotification(tokens, notification, NotificationPriority.normal);
            return ResponseResult.newSuccessInstance(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseResult.newInstance(ResponseCode.FAIL, "error: " + e.getMessage(), null);
        }
    }



    @Override
    public ResponseResult processCreateNotification(String title, String message, String action)
    {
        ResponseResult res = validation.validNotification(title, message);

        if (res != null)
        {
            return res;
        }

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setTitle(title);
        //notification.setType(action);

        notificationRepository.save(notification);
        return ResponseResult.newSuccessInstance(notification);
    }

    @Override
    public ResponseResult processUpdateNotification(NotificationPushDTO notificationPushDTO)
    {

        ResponseResult result = validation.validNotification(notificationPushDTO.getTitle(), notificationPushDTO.getMessage());

        if (result != null)
        {
            return result;
        }

        Notification notification = notificationRepository.findOne(notificationPushDTO.getId());

        if (notification == null)
        {
            return ResponseResult.newInstance(ResponseCode.FAIL, getMessage("notification.not_exists"), null);
        }

        if (notificationPushDTO.getSendTime() < 0)
        {
            return ResponseResult.newErrorInstance("02", this.getMessage("notification.invalid.sendTime"));
        }

        notification.setMessage(notificationPushDTO.getMessage());
        notification.setTitle(notificationPushDTO.getTitle());

        notification.setScreen(MobileScreen.find(notificationPushDTO.getScreen()));

        if (!notification.getStatus().equals(NotificationStatus.PROCESS))
        {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("notification.invalid.process"));
        }

        Date sendTime = new Date(System.currentTimeMillis() + notificationPushDTO.getSendTime()*1000);
        notification.setSendTime(sendTime);

        notification.setType(NotificationType.valueOf(notificationPushDTO.getType()));

        notificationRepository.save(notification);

        return ResponseResult.newSuccessInstance(notification);
    }

    @Override
    public ResponseResult processDeleteNotification(String id)
    {
        Notification notification = notificationRepository.findOne(id);

        if (notification == null)
        {
            return ResponseResult.newInstance(ResponseCode.FAIL, getMessage("notification.invalid.title"), null);
        }

        notificationRepository.delete(notification);
        return ResponseResult.newSuccessInstance(null);
    }

    @Override
    public ResponseResult processGetListNotification(Integer page)
    {

        return ResponseResult.newSuccessInstance(notificationRepository.findNotificationByPage(page));
    }

    @Override
    public ResponseResult processSearchNotification(NotificationSearchDTO notificationSearchDTO, Integer page, Integer pageSize, String uri)
    {

        try
        {
            Map<String, String> params = MappingRedis.convertObjectToMap(notificationSearchDTO);
            List<Notification> notificationList = notificationRepository.searchNotification(page, pageSize, params);
            long total = notificationRepository.count();
            List<NotificationItemDTO> notificationItemDTOList = new ArrayList<>();
            notificationList.forEach(notification ->
            {
                NotificationItemDTO notificationItemDTO = NotificationItemDTO.with(notification);
                notificationItemDTOList.add(notificationItemDTO);
            });

            PageInfo pageInfo = new PageInfo(page, total, pageSize, uri);
            TableList<NotificationItemDTO> tableList = new TableList<>(pageInfo, notificationItemDTOList);

            return ResponseResult.newSuccessInstance(tableList);
        }
        catch (Exception e)
        {
            logger.error("processSearchNotification: ", e);
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, e.getMessage());
        }
    }

    @Override
    public ResponseResult processCancelNotification(String id)
    {
        Notification notification = notificationRepository.findOne(id);
        if (notification != null)
        {
            notification.setStatus(NotificationStatus.CANCEL);
            notificationRepository.save(notification);
        }
        return ResponseResult.newSuccessInstance(notification);
    }

    @Override
    public ResponseResult processGetDetails(String id)
    {
        Notification notification = notificationRepository.findOne(id);
        if (notification != null)
        {
            NotificationDetailsDTO notificationItemDTO = NotificationDetailsDTO.with(notification);
            return ResponseResult.newSuccessInstance(notificationItemDTO);
        }
        else
        {
            return ResponseResult.newErrorInstance("01", this.getMessage("notification.not_exists"));
        }
    }

    @Override
    public ResponseResult processPushNotificationByQuery(NotificationPushDTO notificationPushDTO)
    {
        ResponseResult result = validation.validNotification(notificationPushDTO.getTitle(), notificationPushDTO.getMessage());

        if (result != null) {
            return result;
        }

        if (notificationPushDTO.getSendTime() < 0) {
            return ResponseResult.newErrorInstance("02", this.getMessage("notification.invalid.sendTime"));
        }

        if (!MobileScreen.equals(notificationPushDTO.getScreen())) {
            return ResponseResult.newErrorInstance("01", this.getMessage("notification.invalid.screen"));
        }

        Notification notification = new Notification();
        if (notificationPushDTO.getScreen().equals(MobileScreen.EXAM_DETAILS.name())
                && org.apache.commons.lang3.StringUtils.isNotBlank(notificationPushDTO.getExamCode())) {
            Exam exam = examRepository.findExamByCode(notificationPushDTO.getExamCode());
            if (exam != null) {
                notification.setObjectId(exam.getId());
            } else {
                return ResponseResult.newErrorInstance("01", getMessage("notification.notfound.exam"));
            }
        }

        if (notificationPushDTO.getScreen().equals(MobileScreen.EXAM_ONLINE_WAITING.name())) {
            List<ExamOnline> examOnlineList = examOnlineRepository.findByStatusOrderByCreatedDateDesc(OnlineTestStatus.OPENING);
            if (examOnlineList.size() > 0) {
                ExamOnline examOnline = examOnlineList.get(0);
                notification.setObjectId(examOnline.getId());
            } else {
                return ResponseResult.newErrorInstance("01", getMessage("notification.notfound.exam"));
            }
        }

        notification.setMessage(notificationPushDTO.getMessage());
        notification.setTitle(notificationPushDTO.getTitle());

        notification.setScreen(MobileScreen.find(notificationPushDTO.getScreen()));
        notification.setStatus(NotificationStatus.PROCESS);

        Date sendTime = new Date(System.currentTimeMillis() + notificationPushDTO.getSendTime()*1000);
        notification.setSendTime(sendTime);

        notification.setType(NotificationType.valueOf(notificationPushDTO.getType()));

        if (notificationPushDTO.getSendTime() == 0)
        {
            if (!org.apache.commons.lang3.StringUtils.isNotBlank(notification.getSendTo()))
            {
                pushListDevicesToQueue(notification);
            }
            else
            {
                User user = userRepository.findByCode(notification.getSendTo());
                if (user != null)
                {
                    List<Device> deviceList = deviceRepository.findDeviceByUserId(user.getId());
                    if (deviceList != null && !deviceList.isEmpty())
                    {
                        notification.setSendTo(user.getId());
                        this.pushSingleDeviceToQueue(notification, deviceList);
                    }
                    else
                    {
                        notification.setStatus(NotificationStatus.ERROR);

                    }
                }
                else
                {
                    notification.setStatus(NotificationStatus.ERROR);
                }
            }

        }

        notificationRepository.save(notification);

        return ResponseResult.newSuccessInstance(null);
    }

    @Override
    @Async
    public void pushSingleDeviceToQueue(Notification notification, List<Device> deviceList)
    {
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


    @Async
    public void pushListDevicesToQueue(Notification notification)
    {
        List<Device> androidDeviceList = deviceRepository.findDeviceByDeviceOs(Os.ANDROID.name());
        int total = androidDeviceList.size();
        List<String> deviceListInQueue = new ArrayList<>(Constant.Notification.MAX_DEVICES);

        int counter = 0;
        for (Device device: androidDeviceList)
        {
            counter++;
            deviceListInQueue.add(device.getDeviceToken());
            int partialSize = deviceListInQueue.size();
            if (partialSize == Constant.Notification.MAX_DEVICES || counter == total)
            {
                try
                {
                    NotificationQueue queue = new NotificationQueue();
                    queue.setDeviceList(deviceListInQueue);
                    queue.setOsVersion(Os.ANDROID.name());
                    queue.setNotification(notification);
                    String jsonArr = mapper.writeValueAsString(queue);
                    rabbitProducer.sendNotification(jsonArr);
                }
                catch (Exception e)
                {
                    logger.error("RabbitMQ exception pushListDevicesToQueue: ", e);
                }
                deviceListInQueue.clear();
            }
        }

        logger.info("Queued message to " + total + " Android devices");

        List<Device> iosDeviceList = deviceRepository.findDeviceByDeviceOs(Os.IOS.name());
        total = iosDeviceList.size();
        deviceListInQueue = new ArrayList<>(Constant.Notification.MAX_DEVICES);

        counter = 0;
        for (Device device: iosDeviceList)
        {
            counter++;
            deviceListInQueue.add(device.getDeviceToken());
            int partialSize = deviceListInQueue.size();
            if (partialSize == Constant.Notification.MAX_DEVICES || counter == total)
            {
                try
                {
                    NotificationQueue queue = new NotificationQueue();
                    queue.setDeviceList(deviceListInQueue);
                    queue.setOsVersion(Os.IOS.name());
                    queue.setNotification(notification);
                    String jsonArr = mapper.writeValueAsString(queue);
                    rabbitProducer.sendNotification(jsonArr);
                }
                catch (Exception e)
                {
                    logger.error("RabbitMQ exception: ", e);
                }
                deviceListInQueue.clear();
                if (counter == total)
                {
                    notification.setStatus(NotificationStatus.SUCCESS);
                    notificationRepository.save(notification);
                }
            }
        }

        logger.info("Queued message to " + total + " IOS devices");
    }
}
