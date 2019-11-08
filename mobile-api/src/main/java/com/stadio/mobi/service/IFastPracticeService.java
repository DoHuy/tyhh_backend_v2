package com.stadio.mobi.service;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.model.model.UserAnswer;

import java.util.List;

/**
 * Created by Andy on 03/02/2018.
 */
public interface IFastPracticeService
{
    ResponseResult processGetRandomQuestion(String token, String sessionId);

    ResponseResult processSubmitQuestion(String token, String sessionId, List<UserAnswer> answers);

    ResponseResult processGetRandomQuestionWithPayment(String token);
}