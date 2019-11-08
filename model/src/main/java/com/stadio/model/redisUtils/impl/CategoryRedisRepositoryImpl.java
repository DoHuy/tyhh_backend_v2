package com.stadio.model.redisUtils.impl;

import com.stadio.common.utils.JsonUtils;
import com.stadio.model.documents.Category;
import com.stadio.model.dtos.mobility.CategoryItemDTO;
import com.stadio.model.redisUtils.CategoryRedisRepository;
import com.stadio.model.redisUtils.RedisConst;
import com.stadio.model.redisUtils.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CategoryRedisRepositoryImpl implements CategoryRedisRepository {
    @Autowired
    RedisRepository redisRepository;

    @Override
    public void processPutCategory(Category category) {
        redisRepository.select(RedisConst.DB_CATEGORY);

        redisRepository.hput(RedisConst.CATEGORY,category.getId(), JsonUtils.pretty(new CategoryItemDTO(category)));

        redisRepository.expire(RedisConst.CATEGORY,RedisConst.TIME_TO_LIVE_TOO_LONG);
    }

    @Override
    public void processPutAllCategory(List<Category> categoryList) {
        redisRepository.select(RedisConst.DB_CATEGORY);
        categoryList.forEach(category ->{
            redisRepository.hput(RedisConst.CATEGORY,category.getId(), JsonUtils.pretty(new CategoryItemDTO(category)));
        });
        redisRepository.expire(RedisConst.CATEGORY,RedisConst.TIME_TO_LIVE_TOO_LONG);
    }

    @Override
    public void processDeleteCategory(String id) {
        redisRepository.select(RedisConst.DB_CATEGORY);

        redisRepository.hdelete(RedisConst.CATEGORY,id);
    }

    @Override
    public Map<String, String> processGetCategory() {
        redisRepository.select(RedisConst.DB_CATEGORY);

        Map<String,String> mapCategories = redisRepository.hgetAll(RedisConst.CATEGORY);
        return mapCategories;
    }
}
