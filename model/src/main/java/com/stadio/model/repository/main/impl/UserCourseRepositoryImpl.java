package com.stadio.model.repository.main.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.stadio.model.documents.UserCourse;
import com.stadio.model.dtos.mobility.course.CourseRateCountDTO;
import com.stadio.model.enu.UserCourseAction;
import com.stadio.model.repository.main.custom.UserCourseRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Iterator;

public class UserCourseRepositoryImpl implements UserCourseRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Iterable<DBObject> getRatingCourseCount(String courseId) {

        BasicDBObject basicDBObject = new BasicDBObject();

        TypedAggregation<UserCourse> agg = Aggregation.newAggregation(UserCourse.class,
                Aggregation.match(Criteria.where("course_id_ref").is(courseId).andOperator(Criteria.where("action").is(UserCourseAction.RATE))),
                Aggregation.group("extendInfo").count().as("total"),
                Aggregation.project().and("_id").as("rate").and("total").as("total")

        ).withOptions(new AggregationOptions(false,false,basicDBObject));
        AggregationResults<CourseRateCountDTO> result = mongoTemplate.aggregate(agg,CourseRateCountDTO.class);

        DBObject dbObjectResult = result.getRawResults();
        BasicDBObject cursor =  ((BasicDBObject)dbObjectResult.get("cursor"));
        Iterable<DBObject> firstBatch = (Iterable)cursor.get("firstBatch");

        Iterator var7 = firstBatch.iterator();

//        while(var7.hasNext()) {
//            DBObject dbObject = (DBObject)var7.next();
//
//            CourseRateCountDTO courseRateCountDTO = new CourseRateCountDTO();
//            courseRateCountDTO.setRate(dbObject.get("rate"));
//            Integer position = (Integer)dbObject.get("_id");
//            Long amount = (Long)dbObject.get("amount");
//            transactionStaticResultList.put(position,amount);
//        }
        return firstBatch;


    }
}
