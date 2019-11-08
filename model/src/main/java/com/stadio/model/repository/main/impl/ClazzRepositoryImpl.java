package com.stadio.model.repository.main.impl;

import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Clazz;
import com.stadio.model.repository.main.custom.ClazzRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class ClazzRepositoryImpl implements ClazzRepositoryCustom {

    @Autowired
    MongoTemplate template;

    @Override
    public Clazz findOneById(String id) {
        if (StringUtils.isNotNull(id)) {
            try {
                Query query = new Query();
                query.addCriteria(
                        Criteria.where("id").is(id)
                );
                return template.find(query,Clazz.class).get(0);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public List<Clazz> findClazzRelateToUserId(String userId) {
        return null;
    }

}
