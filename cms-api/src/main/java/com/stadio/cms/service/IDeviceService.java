package com.stadio.cms.service;

import com.stadio.cms.response.ResponseResult;
import com.stadio.model.dtos.cms.DeviceSearchDTO;


/**
 * Created by Andy on 02/16/2018.
 */
public interface IDeviceService
{
    ResponseResult processSearchDevice(DeviceSearchDTO deviceSearchDTO, Integer page, Integer pageSize, String uri);
}
