package com.stadio.mobi.controllers;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/config")
public class ConfigController extends BaseController
{
    @Autowired
    IConfigService configService;

    @GetMapping()
    public ResponseEntity getConfig()
    {
        ResponseResult result = configService.processGetMobileConfig();
        return ResponseEntity.ok(result);
    }
}
