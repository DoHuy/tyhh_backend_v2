package com.stadio.model.redisUtils;

import com.stadio.model.documents.Category;

import java.util.List;
import java.util.Map;

public interface CategoryRedisRepository {
    void processPutCategory(Category category);

    void processPutAllCategory(List<Category> categoryList);

    void processDeleteCategory(String id);

    Map<String,String> processGetCategory();
}
