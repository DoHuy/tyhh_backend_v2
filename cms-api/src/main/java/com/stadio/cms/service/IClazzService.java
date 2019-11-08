package com.stadio.cms.service;

import com.stadio.cms.response.ResponseResult;
import com.stadio.model.dtos.cms.ClazzFormDTO;

public interface IClazzService {
    ResponseResult processCreateOneClazz(ClazzFormDTO clazzFormDTO);

    ResponseResult ProcessUpdateOneClazz(ClazzFormDTO clazzFormDTO);

    ResponseResult ProcessGetClazzById(String id);

    ResponseResult ProcessGetAllClazz();

    ResponseResult ProcessDeleteClazz(String id);
}
