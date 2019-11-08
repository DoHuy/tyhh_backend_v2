package com.stadio.mobi.controllers;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Andy on 01/14/2018.
 */
@RestController()
@RequestMapping(value = "api/rank")
public class RankController extends BaseController
{
    @Autowired IRankService rankService;

    @GetMapping()
    public ResponseEntity getRankByType()
    {
        ResponseResult result = rankService.processBuildUserRank();
        return ResponseEntity.ok(result);
    }



}
