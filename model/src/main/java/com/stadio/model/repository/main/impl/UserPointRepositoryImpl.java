package com.stadio.model.repository.main.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.stadio.model.documents.UserPoint;
import com.stadio.model.dtos.cms.UserPointMongoDTO;
import com.stadio.model.repository.main.custom.UserPointRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

public class UserPointRepositoryImpl implements UserPointRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Iterable<DBObject> groupPointByUser(Integer year, Integer month) {

        BasicDBObject basicDBObject = new BasicDBObject();

        TypedAggregation<UserPoint> agg = Aggregation.newAggregation(UserPoint.class,
                Aggregation.match(Criteria.where("point").gt(0)),
                Aggregation.project("createdDate","point", "userId")
                .andExpression("month(createdDate)").as("month")
                .andExpression("year(createdDate)").as("year"),
        Aggregation.match(Criteria.where("month").is(month).and("year").is(year)),
        Aggregation.group("userId").sum("point").as("total"),
                Aggregation.limit(100),
                Aggregation.sort(Sort.Direction.DESC, "total")
        ).withOptions(new AggregationOptions(false,false,basicDBObject));
        AggregationResults<UserPointMongoDTO> result = mongoTemplate.aggregate(agg,UserPointMongoDTO.class);

        DBObject dbObjectResult = result.getRawResults();
        BasicDBObject cursor =  ((BasicDBObject)dbObjectResult.get("cursor"));
        Iterable<DBObject> firstBatch = (Iterable)cursor.get("firstBatch");

        return firstBatch;
    }
}
