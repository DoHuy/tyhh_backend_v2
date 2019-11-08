package com.stadio.mobi.service;

import com.stadio.mobi.dtos.ExamSubmitDTO;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.model.model.UserAnswer;

import java.util.List;

public interface IPracticeService
{

    ResponseResult processGetPractice();

    ResponseResult processSubmitResults(String token, ExamSubmitDTO examSubmitDTO, String examId);

    ResponseResult processGetAnswers(String token, String examId);
}
