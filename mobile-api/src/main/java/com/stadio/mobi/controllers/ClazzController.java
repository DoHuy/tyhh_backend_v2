package com.stadio.mobi.controllers;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/clazz")
public class ClazzController extends BaseController {

    @Autowired
    IClazzService clazzService;

    @GetMapping(value = "/all")
    public ResponseEntity getClassList()
    {
        ResponseResult result = clazzService.processGetAllClazz();
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/examByClazz")
    public ResponseEntity getExamByClazz(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(value = "clazzId") String clazzId,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "limit", defaultValue = "10", required = false) int limit)
    {
        ResponseResult result = clazzService.processGetExamByClazzId(token, clazzId, page, limit);
        return ResponseEntity.ok(result);
    }
}
