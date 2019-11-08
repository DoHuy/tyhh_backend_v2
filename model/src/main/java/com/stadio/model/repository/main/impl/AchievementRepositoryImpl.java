package com.stadio.model.repository.main.impl;

import com.stadio.model.repository.main.custom.AchievementRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Set;

public class AchievementRepositoryImpl implements AchievementRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public <T> List<T> achievementCheckResult(String raw, Class<T> ck) throws Exception {
        Query query = new BasicQuery(raw);
        List<T> results = mongoTemplate.find(query, ck);



        return results;
    }

    @Override
    public Set<String> getAllCollections() {
        return mongoTemplate.getCollectionNames();
    }

}
