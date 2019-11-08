package com.stadio.model.repository.main.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.stadio.model.documents.UserExam;
import com.stadio.model.dtos.mobility.UserExamOnlineGroupByCorrectNumberDTO;
import com.stadio.model.repository.main.custom.UserExamRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

/**
 * Created by Andy on 02/28/2018.
 */
public class UserExamRepositoryImpl implements UserExamRepositoryCustom
{

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Iterable<DBObject> groupByCorrectNumber(String examId) {
        BasicDBObject basicDBObject = new BasicDBObject();

        TypedAggregation<UserExam> agg = Aggregation.newAggregation(UserExam.class,
                Aggregation.match(Criteria.where("examIdRef").is(examId)),
                Aggregation.group("correctNumber").count().as("userCorrectNumber")
        ).withOptions(new AggregationOptions(false,false,basicDBObject));
        AggregationResults<UserExamOnlineGroupByCorrectNumberDTO> result = mongoTemplate.aggregate(agg,UserExamOnlineGroupByCorrectNumberDTO.class);

        DBObject dbObjectResult = result.getRawResults();
        BasicDBObject cursor =  ((BasicDBObject)dbObjectResult.get("cursor"));
        Iterable<DBObject> firstBatch = (Iterable)cursor.get("firstBatch");

        return firstBatch;
    }
}
