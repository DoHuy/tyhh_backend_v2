package com.stadio.mobi.controllers;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Andy on 01/14/2018.
 */
@RestController
@RequestMapping(value = "api/category")
public class CategoryController extends BaseController {
    @Autowired
    ICategoryService categoryService;

    @GetMapping(value = "/details")
    public ResponseEntity getCategoryDetails(
            @RequestParam(value = "id") String id,
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        ResponseResult result = categoryService.processGetDetailCategory(id, token);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/detail")
    public ResponseEntity getCategoryDetail(@RequestParam(value = "id") String id) {
        ResponseResult result = categoryService.processGetDetail(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/examList")
    public ResponseEntity getExamInCategory(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "pageSize") int pageSize
    ) {
        ResponseResult result = categoryService.processGetExamInCategory(id, page, pageSize);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/like")
    public ResponseResult likeCourse(
            @RequestParam(value = "categoryId") String categoryId
    ){
        return categoryService.likeCategory(categoryId);
    }

    @GetMapping(value = "/register")
    public ResponseResult register(
            @RequestParam(value = "categoryId") String categoryId
    ){
        return categoryService.register(categoryId);
    }

    @GetMapping(value = "/other")
    public ResponseResult recommend(
            @RequestParam(value = "currentCategoryId", required = false) String categoryId,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "limit", defaultValue = "20", required = false) int limit
    ){
        return categoryService.recommend(categoryId, page, limit);
    }

}
