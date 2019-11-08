package com.stadio.cms.service;

import com.stadio.cms.response.ResponseResult;
import com.stadio.model.dtos.cms.ExamHotFormDTO;

public interface IHotExamService
{
    ResponseResult processSearchExam(String q);

    ResponseResult processCreateExamHot(ExamHotFormDTO examHotFormDTO);

    ResponseResult processUpdateExamHot(ExamHotFormDTO examHotFormDTO);

    ResponseResult processDeleteExamHot(String id);

    ResponseResult processGetListExamHot(String topType);
}
