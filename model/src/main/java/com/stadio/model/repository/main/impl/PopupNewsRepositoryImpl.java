package com.stadio.model.repository.main.impl;

import com.stadio.model.documents.PopupNews;
import com.stadio.model.repository.main.custom.PopupNewsRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class PopupNewsRepositoryImpl implements PopupNewsRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<PopupNews> findAllByShowInAppSorted() {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "updated_date").
                and(new Sort(Sort.Direction.ASC,"show_in_app")));

        return mongoTemplate.find(query, PopupNews.class);
    }
}
