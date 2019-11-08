package com.stadio.mobi.controllers;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IUserPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping(value = "api/userPoint")
public class UserPointController {

    @Autowired
    IUserPointService userPointService;

    @GetMapping(value = "/details")
    public ResponseEntity getUserPoint(
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "month", required = false) String month
    ) {
        ResponseResult result = userPointService.processGetUserPoint(userId, month);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/rank")
    public ResponseEntity getRank(
            @RequestParam(value = "month") String month
    ) throws ParseException {
        ResponseResult result = userPointService.processGetRank(month);
        return ResponseEntity.ok(result);
    }
}
