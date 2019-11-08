package com.stadio.cms.service;

import com.stadio.cms.response.ResponseResult;
import com.stadio.model.dtos.cms.TeacherFormDTO;

public interface ITeacherService {
    ResponseResult processCreateOneTeacher(TeacherFormDTO teacherFormDTO);

    ResponseResult processUpdateOneTeacher(TeacherFormDTO teacherFormDTO);

    ResponseResult processGetAllTeacher();

    ResponseResult processDeleteTeacher(String id);
}
