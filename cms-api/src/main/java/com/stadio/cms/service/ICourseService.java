package com.stadio.cms.service;

import com.stadio.cms.response.ResponseResult;
import com.stadio.model.dtos.cms.CourseFormDTO;
import com.stadio.model.dtos.cms.CourseSearchFormDTO;

public interface ICourseService {

    ResponseResult processSearchCourse(CourseSearchFormDTO courseSearchFormDTO, int page, int pageSize);

    ResponseResult processCreateCourse(CourseFormDTO courseFormDTO);

    ResponseResult processUpdateCourse(CourseFormDTO courseFormDTO);

    ResponseResult processDeleteCourse(String id);

    ResponseResult processGetCourse(String id);
}
