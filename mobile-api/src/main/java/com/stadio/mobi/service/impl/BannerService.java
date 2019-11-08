package com.stadio.mobi.service.impl;

import com.stadio.common.utils.JsonUtils;
import com.stadio.common.utils.ResponseCode;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IBannerService;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.dtos.mobility.BannerItemDTO;
import com.stadio.model.redisUtils.BannerRedisRepository;
import com.stadio.model.repository.main.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Andy on 01/13/2018.
 */
@Service
public class BannerService extends BaseService implements IBannerService
{
    @Autowired BannerRepository bannerRepository;

    @Autowired
    BannerRedisRepository bannerRedisRepository;

    @Override
    public ResponseResult processGetBannersForMobility()
    {
        List<BannerItemDTO> bannerItemDTOS = new LinkedList<>();
        Map<String, String> bannerMap = bannerRedisRepository.processGetBanner();
        if(!bannerMap.isEmpty()){
            List<String> values = new ArrayList<>(bannerMap.values());
            values.forEach(value-> {
                BannerItemDTO bannerItemDTO = JsonUtils.parse(value, BannerItemDTO.class);
                bannerItemDTOS.add(bannerItemDTO);
            });
        } else {
            List<Banner> bannerList = bannerRepository.findBannerByEnableOrderByPositionAsc(true);
            if(!bannerList.isEmpty()){
                bannerList.forEach(banner -> {
                    BannerItemDTO bannerItemDTO = BannerItemDTO.newInstance(banner);
                    bannerItemDTOS.add(bannerItemDTO);
                });
            }

            bannerRedisRepository.processPutAllBanner(bannerList);
        }

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("home.success.getBanners"), bannerItemDTOS);
    }
}
