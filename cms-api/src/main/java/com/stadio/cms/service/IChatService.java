package com.stadio.cms.service;

import com.stadio.cms.dtos.PushMessageDTO;
import com.stadio.cms.response.ResponseResult;

/**
 * Created by Andy on 03/04/2018.
 */
public interface IChatService
{
    ResponseResult processPushMessageNow(PushMessageDTO pushMessageDTO);
}
