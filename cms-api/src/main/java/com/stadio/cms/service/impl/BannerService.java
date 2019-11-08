package com.stadio.cms.service.impl;

import com.stadio.common.utils.ResponseCode;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IBannerService;
import com.stadio.model.documents.Banner;
import com.hoc68.users.documents.Manager;
import com.stadio.model.dtos.cms.BannerFormDTO;
import com.stadio.model.dtos.cms.BannerListDTO;
import com.stadio.model.redisUtils.BannerRedisRepository;
import com.stadio.model.repository.main.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Andy on 01/13/2018.
 */
@Service
public class BannerService extends BaseService implements IBannerService
{
    @Autowired BannerRepository bannerRepository;

    @Autowired
    BannerRedisRepository bannerRedisRepository;

    @Autowired ManagerService managerService;

    @Override
    public ResponseResult processGetBanners()
    {
        List<BannerListDTO> bannerListDTOS = new ArrayList<>();
        for (Banner banner : bannerRepository.findAll())
        {
            bannerListDTOS.add(BannerListDTO.newInstance(banner));
        }
        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("home.success.getBanners"), bannerListDTOS);
    }

    @Override
    public ResponseResult processAddBanner(BannerFormDTO bannerFormDTO,String token)
    {
        Banner banner = new Banner();
        banner.setImageIdRef(bannerFormDTO.getImageId());
        banner.setImageUrl(bannerFormDTO.getUrl());
        banner.setName(bannerFormDTO.getName());
        banner.setActionUrl(bannerFormDTO.getActionUrl());
        if (bannerFormDTO.getStartTime() != 0 && bannerFormDTO.getEndTime() != 0) {
            banner.setStartTime(new Date((long) bannerFormDTO.getStartTime()));
            banner.setEndTime(new Date((long) bannerFormDTO.getEndTime()));
        }
        banner.setEnable(Boolean.valueOf(bannerFormDTO.getIsShow()));
        banner.setDeepLink(bannerFormDTO.getDeeplink());
        banner.setPayload(bannerFormDTO.getPayload());

        Manager manager = managerService.getManagerRequesting();
        banner.setCreatedBy(manager.getId());
        banner.setUpdatedBy(manager.getId());

        Banner banersave = bannerRepository.save(banner);
        if(banner != null && banner.isEnable()) {
            bannerRedisRepository.processPutBanner(banersave);
        }

        return ResponseResult.newInstance(ResponseCode.SUCCESS, "", banner);
    }

    @Override
    public ResponseResult processUpdateBanner(BannerFormDTO bannerFormDTO)
    {
        Banner banner = bannerRepository.findOne(bannerFormDTO.getId());
        if (banner == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL,"banner.not.found");
        }
        banner.setImageIdRef(bannerFormDTO.getImageId());
        banner.setImageUrl(bannerFormDTO.getUrl());
        banner.setName(bannerFormDTO.getName());
        banner.setActionUrl(bannerFormDTO.getActionUrl());
        banner.setStartTime(new Date((long) bannerFormDTO.getStartTime()));
        banner.setEndTime(new Date((long) bannerFormDTO.getEndTime()));
        banner.setEnable(Boolean.valueOf(bannerFormDTO.getIsShow()));
        banner.setDeepLink(bannerFormDTO.getDeeplink());
        banner.setPayload(bannerFormDTO.getPayload());

        banner.setUpdatedDate(new Date());

        Manager manager = managerService.getManagerRequesting();
        banner.setUpdatedBy(manager.getId());

        bannerRepository.save(banner);
        if(banner.isEnable()) {
            bannerRedisRepository.processPutBanner(banner);
        } else {
            bannerRedisRepository.processDeleteBanner(banner.getId());
        }

        return ResponseResult.newInstance(ResponseCode.SUCCESS, "", banner);
    }

    @Override
    public ResponseResult processDeleteBanner(String id)
    {
        bannerRepository.delete(id);
        bannerRedisRepository.processDeleteBanner(id);
        return ResponseResult.newInstance(ResponseCode.SUCCESS, "", "");
    }

}
