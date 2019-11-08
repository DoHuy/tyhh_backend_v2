package com.stadio.model.dtos.cms;

import com.stadio.common.enu.DeepLink;
import lombok.Data;

/**
 * Created by Andy on 12/24/2017.
 */
@Data
public class BannerFormDTO
{
    private String id;
    private String imageId;
    private String name;
    private String url;
    private Boolean isShow;
    private double startTime;
    private double endTime;
    private String actionUrl;
    private DeepLink deeplink;
    private String payload;
}
