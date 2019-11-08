package com.stadio.model.repository.main;

import com.stadio.model.documents.UserCourse;
import com.stadio.model.enu.UserCourseAction;
import com.stadio.model.repository.main.custom.UserCourseRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserCourseRepository extends MongoRepository<UserCourse, String>, UserCourseRepositoryCustom {

    long countByCourseIdRefAndAction(String courseId, UserCourseAction action);

    List<UserCourse> findAllByUserIdRefAndCourseIdRefAndAction(String userId, String courseId, UserCourseAction action);

    List<UserCourse> findAllByUserIdRefAndAction(String userId, UserCourseAction action);

    List<UserCourse> findAllByUserIdRefAndActionOrderByCreatedDateDesc(String userId, UserCourseAction action);

    UserCourse findFirstByUserIdRefAndCourseIdRefAndAction(String userId, String courseId, UserCourseAction action);

}
