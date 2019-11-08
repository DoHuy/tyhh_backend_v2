package com.stadio.cms.service;

import com.stadio.cms.response.ResponseResult;
import com.stadio.model.documents.Exam;
import com.stadio.model.dtos.cms.QuestionFormDTO;
import com.stadio.model.dtos.cms.QuestionSearchFormDTO;

/**
 * Created by sm on 12/14/17.
 */
public interface IQuestionService {

    ResponseResult processCreateOneQuestion(QuestionFormDTO questionFormDTO, String token);

    ResponseResult processCreateOneQuestion(Exam exam, Integer nth, QuestionFormDTO questionFormDTO, String token);

    ResponseResult processUpdateOneQuestion(QuestionFormDTO questionFormDTO, String token);

    ResponseResult processUpdateOneQuestion(Exam exam, Integer nth, QuestionFormDTO questionFormDTO, String token);

    ResponseResult processSearchQuestion(QuestionSearchFormDTO questionSearchFormDTO, Integer page, Integer pageSize, String uri);

    ResponseResult processGetQuestionById(String id);

    ResponseResult processDeleteOneQuestion(String id);

}
