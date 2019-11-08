package com.stadio.mobi.service;

import com.stadio.mobi.dtos.examOnline.ExamOnlineSubmitDTO;
import com.stadio.mobi.response.ResponseResult;

public interface IOnlineTestService {

    ResponseResult processGetListExamOnlineOpening();

    ResponseResult processUserSubscribeExamOnline(String id);

    ResponseResult processUserCancelExamOnline(String id);

    ResponseResult processGetQuestionList(String id);

    ResponseResult processGetDetails(String id);

    ResponseResult processGetListExamOnlineFinished(int page, int limit);

    ResponseResult processGetTablePointOfExamOnline(String id);

    ResponseResult processGetResultsOfExamOnline(String id);

    ResponseResult processAllowUserCanLikeIt(String id);

    ResponseResult processSubmitExamOnline(String id, ExamOnlineSubmitDTO examOnlineSubmitDTO);
}
