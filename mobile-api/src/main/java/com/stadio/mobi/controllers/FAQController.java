package com.stadio.mobi.controllers;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IFAQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/faq")
public class FAQController extends BaseController {

    @Autowired
    IFAQService ifaqService;

    @GetMapping(value = "/list")
    ResponseResult getListFAQ(){
        return ifaqService.processGetListFAQ();
    }
}
