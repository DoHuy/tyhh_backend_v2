package com.stadio.cms.controllers.content;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.ISearchService;
import com.stadio.model.documents.SearchKeywords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Andy on 01/09/2018.
 */
@RestController
@RequestMapping(value = "api/search", name = "Quản lý Tím kiếm")
public class SearchController extends BaseController
{
    @Autowired ISearchService searchService;

    @PostMapping(value = "/insertKeywords", name = "Thêm từ khóa tìm kiếm")
    public ResponseEntity insertKeyWords(@RequestParam(value = "key") String key)
    {
        SearchKeywords searchKeywords = searchService.processSaveKeyWord(key);
        return ResponseEntity.ok(ResponseResult.newSuccessInstance(searchKeywords));
    }

    @GetMapping(value = "/listKeyWords", name = "Danh sách từ khóa tìm kiếm")
    public ResponseEntity listKeyWords()
    {
        return ResponseEntity.ok(ResponseResult.newSuccessInstance(searchService.getKeywords()));
    }

    @GetMapping(value = "/delete", name = "Xóa từ khóa")
    public ResponseEntity deleteKey(@RequestParam("id") String id)
    {
        searchService.processDeleteKeyword(id);
        ResponseResult result = ResponseResult.newSuccessInstance(null);
        return ResponseEntity.ok(ResponseResult.newSuccessInstance(result));
    }

}
