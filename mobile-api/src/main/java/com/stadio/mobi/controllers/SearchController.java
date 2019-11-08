package com.stadio.mobi.controllers;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.ISearchService;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Andy on 01/14/2018.
 */
@RestController
@RequestMapping(value = "api/search")
public class SearchController extends BaseController
{
    @Autowired ISearchService searchService;

    @GetMapping("/listKeywords")
    public ResponseEntity listKeywords()
    {
        List<SearchKeywords> keywords = searchService.getKeywords();
        ResponseResult result = ResponseResult.newSuccessInstance(keywords);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/action")
    public ResponseEntity actionSearch(
            @RequestParam(value = "keyword", required = true, defaultValue = "") String keyword)
    {
        ResponseResult result = searchService.processSearch(keyword);
        return ResponseEntity.ok(result);
    }

}
