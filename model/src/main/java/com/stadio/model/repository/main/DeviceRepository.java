package com.stadio.model.repository.main;

import com.stadio.model.documents.Device;
import com.stadio.model.repository.main.custom.DeviceRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DeviceRepository extends MongoRepository<Device, String>, DeviceRepositoryCustom
{
    List<Device> findDeviceByDeviceOs(String os);

    List<Device> findDeviceByUserId(String userId);

    List<Device> findDeviceByUserIdIn(List<String> userIdList);
}
