package com.stadio.mobi.service;

import com.stadio.mobi.response.ResponseResult;

/**
 * Created by sm on 12/14/17.
 */
public interface IQuestionService
{

    ResponseResult processGetQuestionForPractice(String token, String examId);
}
