package com.stadio.mobi.service;

import com.stadio.mobi.response.ResponseResult;

public interface IHotExamService {

    ResponseResult processGetHotExams(String token, String topType);
}
