package com.stadio.model.dtos.cms;

import lombok.Data;

/**
 * Created by Andy on 02/16/2018.
 */
@Data
public class DeviceSearchDTO
{
    private String deviceId;
    private String deviceOs;
    private String version;
    private String model;
}
