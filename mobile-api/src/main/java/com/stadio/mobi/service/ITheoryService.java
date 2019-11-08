package com.stadio.mobi.service;

import com.stadio.mobi.response.ResponseResult;

public interface ITheoryService {

    ResponseResult findByChapterId(String id);

    ResponseResult markAsRead(String theoryId);

    ResponseResult getTheoryDetailsById(String theoryId);
}
