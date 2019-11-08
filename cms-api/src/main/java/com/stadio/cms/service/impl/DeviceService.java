package com.stadio.cms.service.impl;

import com.stadio.cms.model.PageInfo;
import com.stadio.common.utils.ResponseCode;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.response.TableList;
import com.stadio.cms.service.IDeviceService;
import com.stadio.model.documents.Device;
import com.stadio.model.dtos.cms.DeviceSearchDTO;
import com.stadio.model.redisUtils.MappingRedis;
import com.stadio.model.repository.main.DeviceRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Andy on 02/16/2018.
 */
@Service
public class DeviceService extends BaseService implements IDeviceService
{

    @Autowired DeviceRepository deviceRepository;

    private Logger logger = LogManager.getLogger(DeviceService.class);
    @Override
    public ResponseResult processSearchDevice(DeviceSearchDTO deviceSearchDTO, Integer page, Integer pageSize, String uri)
    {
        try
        {
            Map<String, String> searchParams = MappingRedis.convertObjectToMap(deviceSearchDTO);
            List<Device> deviceList = deviceRepository.searchDevice(page, pageSize, searchParams);

            long deviceTotal = deviceRepository.count();

            PageInfo pageInfo = new PageInfo(page, deviceTotal, pageSize, uri);

            TableList<Device> tableList = new TableList<>(pageInfo, deviceList);

            return ResponseResult.newSuccessInstance(tableList);
        }
        catch (Exception e)
        {
            logger.error("SearchDevice Exception: ", e);
        }
        return ResponseResult.newErrorInstance(ResponseCode.FAIL, "Server Internal Error");
    }
}
