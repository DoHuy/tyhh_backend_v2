package com.stadio.mobi.service;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.model.dtos.mobility.ExamItemDTO;

public interface IExamService
{

    ResponseResult processGetExamDetailForMobile(String id);

    ResponseResult processGetRecommend(String token);

    ResponseResult processGetHighlight(String token);

    ResponseResult processGetNewestExam(String token, Integer page, Integer limit);

    ResponseResult processUserActionLike(String token, String examId);

    ResponseResult processGetExamTopSubmit(String token, int page, int limit);

    void setPriceAndDidDone(ExamItemDTO examItemDTO);

    boolean isUserDidDone(String examId);
}
