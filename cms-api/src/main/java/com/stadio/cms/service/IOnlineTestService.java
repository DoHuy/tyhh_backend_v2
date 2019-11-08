package com.stadio.cms.service;

import com.stadio.cms.dtos.examOnline.ExamOnlineTestForm;
import com.stadio.cms.response.ResponseResult;

public interface IOnlineTestService {

    ResponseResult processCreateOrUpdateExamOnlineTest(ExamOnlineTestForm examOnlineTestForm);

    ResponseResult processCancelExamOnlineTest(String id);

    ResponseResult processGetListExamOnline(int page, int limit);

    ResponseResult processGetShortInformation(String id);

    ResponseResult processOpeningRegister(String id);

    ResponseResult processDetailsExamOnlineTest(String id);

    ResponseResult processGetListJoinerExamOnline(String id, int page, int limit);

    ResponseResult processGetTablePoint(String id, int page, int limit);

    ResponseResult processPushMessageRemind(String id);

    ResponseResult processPushMessageResults(String id);
}
