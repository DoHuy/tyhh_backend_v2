package com.stadio.es.controllers;

import com.stadio.es.response.ResponseResult;
import com.stadio.es.service.IExamService;
import com.stadio.es.service.ISearchService;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.elasticsearch.action.get.GetResponse;

@RestController
@RequestMapping(value = "api/search")
public class SearchController {

    @Autowired
    ISearchService searchService;

    @Autowired
    IExamService examService;

    @Autowired
    private TransportClient client;



    @RequestMapping(value = "/action",method = RequestMethod.GET)
    public ResponseResult searchExam(@RequestParam(value = "keyword") String keyword){
        return searchService.processSearchExam(keyword);
    }

    @RequestMapping(value = "/exam/{id}")
    public GetResponse test(@PathVariable String id) {
        GetResponse response = client.prepareGet("subjects", "exam", id).get();
        return response;
    }

    @GetMapping(value = "/chemical-equation")
    public ResponseResult search(@RequestParam(value = "content") String content){
        return searchService.processSearchChemicalEquation(content);
    }

    @Autowired
    public void resetData(){
        examService.resetData();
    }
}
