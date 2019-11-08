package com.stadio.model.redisUtils;

import com.stadio.model.documents.Banner;

import java.util.List;
import java.util.Map;

public interface BannerRedisRepository {
    void processPutBanner(Banner banner);

    void processPutAllBanner(List<Banner> bannerList);

    void processDeleteBanner(String id);

    Map<String, String> processGetBanner();
}
