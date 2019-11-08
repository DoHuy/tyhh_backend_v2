package com.stadio.cms.service;

import com.stadio.cms.response.ResponseResult;
import com.stadio.model.documents.Exam;
import com.stadio.model.dtos.cms.ExamFormDTO;
import com.stadio.model.dtos.cms.ExamHotFormDTO;
import com.stadio.model.dtos.cms.ExamSearchFormDTO;

public interface IExamService
{

    ResponseResult processCreateOneExam(ExamFormDTO examFormDTO, String token);

    ResponseResult processUpdateOneExam(ExamFormDTO examFormDTO, String token);

    ResponseResult processDeleteOneExam(String id, String token);

    ResponseResult processGetListExam(String token, Integer page, Integer pageSize, String uri);

    ResponseResult processGetExamDetail(String id);

    Exam findOne(String id);

    Void save(Exam exam);

    ResponseResult processSearchExam(String token, ExamSearchFormDTO examSearchFormDTO, Integer page, Integer pageSize, String uri);

    ResponseResult processGetExamDetailForCMS(String id);

    ResponseResult getQuestionNthOfExam(String id, Integer nth);

    ResponseResult getQuestionQuantityOfExam(String id);

    ResponseResult getQuestionOfExam(String id);



}
