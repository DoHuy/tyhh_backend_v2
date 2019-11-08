package com.stadio.model.repository.main.impl;

import com.stadio.model.documents.ExamHot;
import com.stadio.model.repository.main.custom.ExamHotRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class ExamHotRepositoryImpl implements ExamHotRepositoryCustom
{

    @Autowired MongoTemplate mongoTemplate;

    @Override
    public List<ExamHot> findAllExamHotWithPositionLessThan(Integer position)
    {
        Criteria criteria = new Criteria();

        Query query = new Query();

        query.addCriteria(
                Criteria.where("position").gt(position)
        );

        return mongoTemplate.find(query, ExamHot.class);
    }
}
