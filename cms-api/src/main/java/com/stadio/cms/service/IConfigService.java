package com.stadio.cms.service;

import com.stadio.cms.response.ResponseResult;

public interface IConfigService
{
    ResponseResult processPutValue(String key, String value, String name);

    ResponseResult processGetListConfig();

    ResponseResult processGetDetails(String key);
}
