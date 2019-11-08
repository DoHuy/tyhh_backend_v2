package com.stadio.cms.service;

import com.stadio.cms.response.ResponseResult;
import com.stadio.model.dtos.cms.ParagraphFormDTO;

public interface IParagraphService {
    ResponseResult processGetDetailOfLecture(String lectureId);

    ResponseResult processCreateDetail(ParagraphFormDTO paragraphFormDTO);

    ResponseResult processUpdateDetail(ParagraphFormDTO paragraphFormDTO);

}
