package com.stadio.mobi.service;

import com.stadio.mobi.dtos.PushMessageDTO;
import com.stadio.mobi.response.ResponseResult;

/**
 * Created by Andy on 03/04/2018.
 */
public interface IChatService
{
    ResponseResult processGetListMessage(String token);

    ResponseResult processPushMessageNow(PushMessageDTO pushMessageDTO);
}
