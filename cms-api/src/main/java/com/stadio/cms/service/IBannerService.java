package com.stadio.cms.service;

import com.stadio.cms.response.ResponseResult;
import com.stadio.model.dtos.cms.BannerFormDTO;

/**
 * Created by Andy on 01/13/2018.
 */
public interface IBannerService
{
    ResponseResult processGetBanners();

    ResponseResult processAddBanner(BannerFormDTO bannerFormDTO, String token);

    ResponseResult processUpdateBanner(BannerFormDTO bannerFormDTO);

    ResponseResult processDeleteBanner(String id);

}
