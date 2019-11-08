package com.stadio.cms.controllers;

import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IDeviceService;
import com.stadio.model.dtos.cms.DeviceSearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Andy on 02/16/2018.
 */
@RestController
@RequestMapping(value = "api/device", name = "Thông tin thiết bị")
public class DeviceController extends BaseController
{
    @Autowired IDeviceService deviceService;

    @PostMapping(value = "/search", name = "Tìm kiếm thông tin thiết bị")
    public ResponseEntity getDeviceList(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            HttpServletRequest request,
            @RequestBody DeviceSearchDTO deviceSearchDTO) {
        ResponseResult result = deviceService.processSearchDevice(deviceSearchDTO, page, pageSize, this.requestURI(request));
        return ResponseEntity.ok(result);
    }
}
