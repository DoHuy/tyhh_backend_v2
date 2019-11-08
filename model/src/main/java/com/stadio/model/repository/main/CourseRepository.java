package com.stadio.model.repository.main;

import com.stadio.model.documents.Course;
import com.stadio.model.repository.main.custom.CourseRepositoryCustom;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CourseRepository extends MongoRepository<Course, String>,CourseRepositoryCustom {

    Course findFirstByCode(String code);

    Course findFirstByIdAndDeletedIsFalseAndEnableIsTrue(String id);

    Page<Course> findAllByDeletedIsFalseAndEnableIsTrue(Pageable pageable);

    @Query("{ $or: [ { 'clazz_id_ref': ?0 }, { 'is_clazz_all': true }], $and: [ { 'deleted': false }, { 'enable': true } ]}")
    List<Course> findCoursesByClazzIdOrIsClazzAllIsTrueAndDeletedIsFalseAndEnableIsTrueOrderByCreatedDateDesc(String clazzId, Sort sort);

    Page<Course> findByClazzIdInAndIdNotInAndDeletedIsFalseAndEnableIsTrue(List<String> clazzIds, List<String> courseIdsNotContain, Pageable pageable);

    long countByTeacherId(String teacherId);

    List<Course> findAllByTeacherId(String teacherId);
}
