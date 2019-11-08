package com.stadio.model.repository.main.custom;

import com.mongodb.DBObject;
import com.stadio.model.dtos.mobility.course.CourseRateCountDTO;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

public interface UserCourseRepositoryCustom {

    Iterable<DBObject> getRatingCourseCount(String courseId);

}
