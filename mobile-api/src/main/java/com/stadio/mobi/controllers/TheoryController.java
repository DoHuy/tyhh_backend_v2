package com.stadio.mobi.controllers;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.ITheoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "api/theory")
public class TheoryController {

    @Autowired
    ITheoryService theoryService;

    @GetMapping("findByChapterId")
    public ResponseResult findTheoryByChapterId(@RequestParam("chapterId") String chapterId) {
        return theoryService.findByChapterId(chapterId);
    }

    @GetMapping("markAsRead")
    public ResponseResult markAsRead(@RequestParam("theoryId") String theoryId) {
        return theoryService.markAsRead(theoryId);
    }

    @GetMapping("details")
    public ResponseEntity getTheoryDetails(@RequestParam(value = "theoryId") String theoryId) {
        ResponseResult result = theoryService.getTheoryDetailsById(theoryId);
        return ResponseEntity.ok(result);
    }

}
