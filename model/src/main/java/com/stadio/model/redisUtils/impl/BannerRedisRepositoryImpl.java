package com.stadio.model.redisUtils.impl;

import com.stadio.common.utils.JsonUtils;
import com.stadio.model.documents.Banner;
import com.stadio.model.dtos.mobility.BannerItemDTO;
import com.stadio.model.redisUtils.BannerRedisRepository;
import com.stadio.model.redisUtils.RedisConst;
import com.stadio.model.redisUtils.RedisRepository;
import com.stadio.model.redisUtils.RedisRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public class BannerRedisRepositoryImpl implements BannerRedisRepository{

    @Autowired
    RedisRepository redisRepository;

    @Override
    public void processPutBanner(Banner banner) {
        redisRepository.select(RedisConst.DB_BANNER);
        String key = RedisConst.BANNER_HOME;
        redisRepository.hput(key, banner.getId(), JsonUtils.pretty(BannerItemDTO.newInstance(banner)));
        redisRepository.expire(key,RedisConst.TIME_TO_LIVE_TOO_LONG);
    }

    @Override
    public void processPutAllBanner(List<Banner> bannerList) {
        redisRepository.select(RedisConst.DB_BANNER);
        String key = RedisConst.BANNER_HOME;
        bannerList.forEach(banner -> {
            redisRepository.hput(key, banner.getId(), JsonUtils.pretty(BannerItemDTO.newInstance(banner)));
        });
        redisRepository.expire(key,RedisConst.TIME_TO_LIVE_TOO_LONG);
    }

    @Override
    public void processDeleteBanner(String id) {
        redisRepository.select(RedisConst.DB_BANNER);
        redisRepository.hdelete(RedisConst.BANNER_HOME, id);
    }

    @Override
    public Map<String, String> processGetBanner() {
        redisRepository.select(RedisConst.DB_BANNER);
        Map<String, String> bannerMap = redisRepository.hgetAll(RedisConst.BANNER_HOME);
        return bannerMap;
    }
}
