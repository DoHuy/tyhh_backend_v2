package com.stadio.model.repository.user.impl;

import com.hoc68.users.documents.Manager;
import com.stadio.model.repository.main.custom.ManagerRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;

import java.util.List;

public class ManagerRepositoryImpl implements ManagerRepositoryCustom
{

    @Autowired
    @Qualifier("secondaryMongoTemplate")
    MongoTemplate mongoTemplate;

    @Override
    public List<Manager> findManagerByPage(Integer page, Integer pageSize)
    {
        Query query = new Query();

        query.limit(pageSize).skip((page - 1)  * pageSize);
        query.with(new Sort(Sort.Direction.DESC, "created_date"));

        return mongoTemplate.find(query, Manager.class);
    }

    @Override
    public List<Manager> queryWithKeyword(String q)
    {
        TextCriteria criteria = TextCriteria.forDefaultLanguage()
                .matchingAny(q);

        Query query = TextQuery.queryText(criteria)
                .sortByScore().limit(100);

        return mongoTemplate.find(query, Manager.class);
    }
}
