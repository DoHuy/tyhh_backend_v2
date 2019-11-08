package com.stadio.mobi.controllers;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(value = "api/chapter")
public class ChapterController extends BaseController
{
    @Autowired IChapterService chapterService;

    @GetMapping(value = "/list")
    public ResponseEntity chapterList()
    {
        ResponseResult result = chapterService.processChapterList();
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/examByChapterId")
    public ResponseEntity examByChapterId(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(value = "chapterId") String chapterId,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "limit", defaultValue = "100", required = false) int limit)
    {
        ResponseResult result = chapterService.processGetExamListByChapterId(token, chapterId, page, limit);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/examByChapterCode")
    public ResponseEntity examByChapterCode(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(value = "code", required = false, defaultValue = "THPT-QG") String code,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "limit", defaultValue = "100", required = false) int limit)
    {
        ResponseResult result = chapterService.processGetExamListByChapterCode(token, code, page, limit);
        return ResponseEntity.ok(result);
    }
}
