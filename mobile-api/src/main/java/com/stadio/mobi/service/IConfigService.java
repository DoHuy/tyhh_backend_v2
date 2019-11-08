package com.stadio.mobi.service;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;

public interface IConfigService
{
    ResponseResult processGetMobileConfig();

    Config getConfigByKey(String key);
}
