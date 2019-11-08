package com.stadio.mobi.service;

import com.stadio.mobi.response.ResponseResult;

public interface IClazzService
{

    ResponseResult processGetAllClazz();

    ResponseResult processGetExamByClazzId(String token, String clazzId, int page, int limit);

}
