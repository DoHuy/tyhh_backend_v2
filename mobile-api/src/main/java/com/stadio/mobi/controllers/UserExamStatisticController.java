package com.stadio.mobi.controllers;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IUserExamStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/userExamStatistic")
public class UserExamStatisticController {

    @Autowired
    IUserExamStatisticService userExamStatisticService;

    /***
     *  @Description: ban cao diem hon bao nhieu nguoi bai thi
     *  @result: so nguoi thap hon/ so nguoi tham du
     */
    @GetMapping(value = "/type1")
    public ResponseResult type1(@RequestParam(value = "userId") String userId,
                                @RequestParam(value = "examId") String examId) {
        return userExamStatisticService.type1(userId, examId);
    }

    /**
     * @param examId
     * @Description: groupby diem bai thi tu 1 den 10
     * @return: map : {score: so nguoi co score [1->10]}
     */

    @GetMapping(value = "/type2")
    public ResponseResult type2(@RequestParam(value = "userId") String userId, @RequestParam(value = "examId") String examId) {
        return userExamStatisticService.type2(userId, examId);
    }


    /**
     * @Description: so cau dung theo chuong
     * @return: type map :   {so cau dung cua chuong: so cau cua chuong}
     */

    @GetMapping(value = "/type3")
    public ResponseResult type3(@RequestParam(value = "userId") String userId, @RequestParam(value = "examId") String examId) {
        return userExamStatisticService.type3(userId, examId);
    }
}
