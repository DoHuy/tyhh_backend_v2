package com.stadio.cms.service;

import com.stadio.cms.dtos.theory.TheoryFormDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.model.documents.Theory;

public interface ITheoryService {

    ResponseResult findByChapterId(String id);

    ResponseResult findByTheoryId(String id);

    ResponseResult saveTheory(TheoryFormDTO theoryFormDTO);

    ResponseResult deleteTheory(String theoryId);

    ResponseResult findExamSumaryByCode(String examCode);
}
