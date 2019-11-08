package com.stadio.notification.service.impl;

import com.hoc68.users.documents.User;
import com.stadio.common.enu.NotificationPriority;
import com.stadio.common.utils.JsonUtils;
import com.stadio.model.documents.Device;
import com.stadio.model.documents.Notification;
import com.stadio.model.repository.main.DeviceRepository;
import com.stadio.model.repository.user.UserRepository;
import com.stadio.notification.service.IPushNotificationService;
import com.stadio.notification.service.properties.StorageProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class PushNotificationService implements IPushNotificationService
{

    private static String testDeviceToken = "e8M-U4Y-krQ:APA91bFdjFRTpIkRy7T-J2NXwd63WgDqy7kM_stjvJjOI9C4TRjoM-uzFLOrry4Ow14iM4X5NdtEvtXF0DNS48q2uYQm9p-S-2o0ATP35zuj6l2UFgbCrSnIiCz9Y8-JYv3Rqm6zKAvm";

    private Logger logger = LogManager.getLogger(PushNotificationService.class);

    @Autowired
    DeviceRepository deviceRepository;

    @Override
    public void sendPushNotification(List<String> deviceToken, Notification notification, NotificationPriority priority)
    {

        JSONObject bodyJSON = new JSONObject();
        JSONObject data = new JSONObject();

        //JSONObject notificationData = new JSONObject();

        bodyJSON.put("title", notification.getTitle());
        bodyJSON.put("body", notification.getMessage());
        bodyJSON.put("sound", "default");
        bodyJSON.put("content_available", true);
        bodyJSON.put("screen", notification.getScreen().name());
        bodyJSON.put("type", notification.getType().name());
        bodyJSON.put("objectId", notification.getObjectId());

        //notificationData.put("screen", notification.getScreen().name());
        //notificationData.put("type", notification.getType().name());

        data.put("notification", bodyJSON);
        data.put("data", bodyJSON);

        data.put("registration_ids", deviceToken);
        data.put("priority", priority.toString());
        data.put("time_to_live", 60);

        try
        {
            logger.info("FCM request: " + data.toString(2));
            JSONObject res = sendPushNotification(data);
            //Object success = res.get("success");
            logger.info("FCM response: " + res.toString(2));

        }
        catch (Exception e)
        {
            logger.error("Connect exception: " + e);
        }

    }

    @Override
    public void sendPushNotification(List<User> userList, Notification notification) {
        List<String> userIds = new ArrayList<>();
        userList.forEach(user -> {
            userIds.add(user.getId());
        });

        List<Device> deviceList = deviceRepository.findDeviceByUserIdIn(userIds);

        List<String> tokens = new ArrayList<>();

        deviceList.forEach(device -> {
            tokens.add(device.getDeviceToken());
        });

        this.sendPushNotification(tokens, notification, NotificationPriority.normal);
    }

    @Override
    public void sendPushNotification(String userId, Notification notification) {
        List<Device> deviceList = deviceRepository.findDeviceByUserId(userId);

        List<String> tokens = new ArrayList<>();

        deviceList.forEach(device -> {
            tokens.add(device.getDeviceToken());
        });

        this.sendPushNotification(tokens, notification, NotificationPriority.normal);
    }

    private static JSONObject sendPushNotification(JSONObject body) throws Exception
    {

        URL obj = new URL(StorageProperties.FCM_API_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setDoOutput(true);

        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Authorization", "key=" + StorageProperties.FCM_SERVER_KEY);

        OutputStream wr = con.getOutputStream();

        wr.write(body.toString().getBytes("UTF-8"));

        wr.flush();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        JSONObject res = new JSONObject(response.toString());

        return res;
    }

}

