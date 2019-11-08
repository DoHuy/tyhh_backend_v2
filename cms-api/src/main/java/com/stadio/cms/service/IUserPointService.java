package com.stadio.cms.service;

import com.stadio.cms.dtos.UserPointFormDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.model.documents.UserPoint;

import java.text.ParseException;

public interface IUserPointService {

    UserPoint createUserPoint(UserPointFormDTO userPointFormDTO);

    ResponseResult processCreateUserPoint(UserPointFormDTO userPointFormDTO);

    ResponseResult processGetUserPoint(String month, String userId);

    ResponseResult processGetRank(String month) throws ParseException;
}
