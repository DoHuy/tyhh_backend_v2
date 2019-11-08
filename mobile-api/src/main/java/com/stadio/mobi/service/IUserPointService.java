package com.stadio.mobi.service;

import com.stadio.mobi.dtos.UserPointFormDTO;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.model.documents.UserPoint;

import java.text.ParseException;

public interface IUserPointService {

    void createUserPoint(UserPointFormDTO userPointFormDTO);

    ResponseResult processGetUserPoint(String userId, String month);

    ResponseResult processGetRank(String month) throws ParseException;
}
