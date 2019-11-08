package com.stadio.mobi.service;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.model.documents.Course;
import com.stadio.model.documents.Lecture;
import com.stadio.model.dtos.mobility.CourseItemDTO;
import com.stadio.model.dtos.mobility.course.LectureDTO;

import java.util.List;

public interface ICourseService {
    ResponseResult processGetNewestCourse();

    ResponseResult processGetNewestCourseFromMongo();

    ResponseResult processGetNewestListCourse();

    ResponseResult processGetCourseByClass(String classId);

    ResponseResult processGetOtherCourseForRecommendation(String currentCourseId, int page, int pageSize);

    List<CourseItemDTO> getCoursesByClass(String classId);

    ResponseResult getLecturesInCourse(String courseId);

    ResponseResult getCourseDetail(String courseId);

    ResponseResult rateCourse(String courseId, double rate);

    ResponseResult likeCourse(String courseId);

    ResponseResult registerCourse(String courseId);

    ResponseResult getLectureDetail(String lectureId);

    LectureDTO getLectureDTO(Lecture lecture);

    CourseItemDTO getCourseItemDTO(Course course);
}
