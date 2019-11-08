package com.stadio.cms.controllers;


import com.stadio.cms.response.ResponseResult;
import com.stadio.common.enu.DeepLink;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "api/deeplink")
public class DeepLinkController {

    @GetMapping(value = "/types")
    public ResponseResult getBanners()
    {
        List<DeepLink> deeplinkTypes = Arrays.asList(DeepLink.values());
        return ResponseResult.newSuccessInstance(deeplinkTypes);
    }

}
