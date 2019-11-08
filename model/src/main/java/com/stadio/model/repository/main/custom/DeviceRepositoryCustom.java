package com.stadio.model.repository.main.custom;

import com.stadio.model.documents.Device;

import java.util.List;
import java.util.Map;

/**
 * Created by Andy on 02/16/2018.
 */
public interface DeviceRepositoryCustom
{
    List<Device> searchDevice(Integer page, Integer pageSize, Map<String, String> search);
}
