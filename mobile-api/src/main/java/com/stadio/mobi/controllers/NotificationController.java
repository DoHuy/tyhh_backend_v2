package com.stadio.mobi.controllers;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Andy on 01/13/2018.
 */
@RestController
@RequestMapping(value = "api/notification")
public class NotificationController extends BaseController
{

    @Autowired
    INotificationService notificationService;

    @PostMapping(value = "/register")
    public ResponseEntity registerDevice(
            @RequestHeader(value = "Authorization", required = false) String accessToken,
            @RequestParam(value = "os") String os,
            @RequestParam(value = "deviceToken") String deviceToken,
            @RequestParam(value = "deviceModel") String deviceModel,
            @RequestParam(value = "osVersion") String osVersion,
            @RequestParam(value = "deviceID") String deviceID)
    {
        ResponseResult result = notificationService.processRegisterDevice(accessToken, deviceModel, os, osVersion, deviceToken, deviceID);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/schedule")
    public ResponseEntity getListNotificationSchedule(
            @RequestParam(value = "lastSyncTime") long lastSyncTime)
    {
        ResponseResult result = notificationService.processGetListWithLastSyncTime(lastSyncTime);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/me")
    public ResponseEntity getMyNotification(
            @RequestHeader(value = "Authorization", required = false) String accessToken,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit)
    {
        ResponseResult result = notificationService.processListMyNotification(page, limit, accessToken);
        return ResponseEntity.ok(result);
    }

}
