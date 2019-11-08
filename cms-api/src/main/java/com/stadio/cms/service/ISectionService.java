package com.stadio.cms.service;


import com.stadio.cms.response.ResponseResult;
import com.stadio.model.dtos.cms.SectionFormDTO;

public interface ISectionService  {
    ResponseResult processGetListSection(String courseId);

    ResponseResult processCreateSection(SectionFormDTO sectionFormDTO);

    ResponseResult processUpdateSection(SectionFormDTO sectionFormDTO);

    ResponseResult processDeleteSection(String id);
}
