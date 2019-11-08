package com.stadio.mobi.service;

import com.stadio.mobi.dtos.UserRankDTO;
import com.stadio.mobi.response.ResponseResult;

import java.util.List;

/**
 * Created by Andy on 03/02/2018.
 */
public interface IRankService
{
    ResponseResult processBuildUserRank();

    List<UserRankDTO> getListUserRank(String rankId);
}
