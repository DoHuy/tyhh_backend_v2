package com.stadio.mobi.service;

import com.stadio.mobi.response.ResponseResult;

public interface IChapterService
{
    ResponseResult processChapterList();

    ResponseResult processGetExamListByChapterId(String token, String chapterId, int page, int limit);

    ResponseResult processGetExamListByChapterCode(String token, String code, int page, int limit);
}
