package com.stadio.cms.controllers.message;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.dtos.NotificationPushDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.INotificationService;
import com.stadio.model.dtos.cms.NotificationSearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "api/notification", name = "Quản lý Thông báo")
public class NotificationController extends BaseController {

    @Autowired
    INotificationService notificationService;

    @PostMapping(value = "/pushOne")
    public ResponseResult pushNotification(
            @RequestParam("to") String to,
            @RequestParam("title") String title,
            @RequestParam("message") String message,
            @RequestParam("action") String action)
    {
        ResponseResult result = notificationService.processPushNotificationToOneDevice(to, title, message, action);
        return result;
    }

    @PostMapping(value = "/pushAll")
    public ResponseEntity pushNotificationToAll(
            @RequestParam("notificationId") String notificationId){

        ResponseResult result = notificationService.processPushNotificationToAll(notificationId);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/push", name = "Gửi thông báo")
    public ResponseEntity pushNotificationByQuery(
            @RequestBody NotificationPushDTO notificationPushDTO)
    {
        ResponseResult result = notificationService.processPushNotificationByQuery(notificationPushDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/create")
    public ResponseEntity createNotification(
            @RequestParam("title") String title,
            @RequestParam("message") String message,
            @RequestParam("action") String action)
    {
        ResponseResult result = notificationService.processCreateNotification( title, message, action);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/update")
    public ResponseResult updateNotification(
            @RequestBody NotificationPushDTO notificationPushDTO
    )
    {
        ResponseResult result = notificationService.processUpdateNotification(notificationPushDTO);
        return result;
    }

    @PostMapping(value = "/delete")
    public ResponseResult updateNotification(
            @RequestParam("id") String id)
    {
        ResponseResult result = notificationService.processDeleteNotification(id);
        return result;
    }

    @GetMapping(value = "/list")
    public ResponseResult getListNotification(
            @RequestParam(value = "page", defaultValue = "1") Integer page)
    {
        ResponseResult result = notificationService.processGetListNotification(page);
        return result;
    }

    @PostMapping(value = "/search")
    public ResponseEntity getSearchNotification(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            HttpServletRequest request,
            @RequestBody NotificationSearchDTO notificationSearchDTO)
    {
        ResponseResult result = notificationService.processSearchNotification(notificationSearchDTO, page, pageSize, this.requestURI(request));
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/cancel")
    public ResponseEntity cancelNotification(
            @RequestParam(value = "id") String id)
    {
        ResponseResult result = notificationService.processCancelNotification(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/details")
    public ResponseEntity getDetails(
            @RequestParam(value = "id") String id)
    {
        ResponseResult result = notificationService.processGetDetails(id);
        return ResponseEntity.ok(result);
    }
}
