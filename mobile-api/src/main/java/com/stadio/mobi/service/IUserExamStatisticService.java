package com.stadio.mobi.service;

import com.stadio.mobi.response.ResponseResult;

public interface IUserExamStatisticService {
    ResponseResult type1(String userId, String examId);

    ResponseResult type2(String userId,String examId);

    ResponseResult type3(String userId, String examId);
}
