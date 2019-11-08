package com.stadio.mobi.controllers;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/course")
public class CourseController {

    @Autowired
    ICourseService courseService;

    @GetMapping(value = "/class")
    public ResponseResult getCourseNewest(
            @RequestParam(value = "classId", required = false, defaultValue = "") String classId
    ){
        return courseService.processGetCourseByClass(classId);
    }

    @GetMapping(value = "/otherCourses")
    public ResponseResult processGetOtherCourseForRecommendation(
            @RequestParam(value = "currentCourseId", required = false) String currentCourseId,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "limit", defaultValue = "20", required = false) int limit
    ){
        return courseService.processGetOtherCourseForRecommendation(currentCourseId, page, limit);
    }

    @GetMapping(value = "/lectures")
    public ResponseResult getLecutresInCourse(
            @RequestParam(value = "courseId") String courseId
    ){
        return courseService.getLecturesInCourse(courseId);
    }

    @GetMapping(value = "/lecture/detail")
    public ResponseResult rateCourse(
            @RequestParam(value = "lectureId") String lectureId
    ){
        return courseService.getLectureDetail(lectureId);
    }

    @GetMapping(value = "/detail")
    public ResponseResult getDetail(
            @RequestParam(value = "courseId") String courseId
    ){
        return courseService.getCourseDetail(courseId);
    }

    @GetMapping(value = "/rateCourse")
    public ResponseResult rateCourse(
            @RequestParam(value = "courseId") String courseId,
            @RequestParam(value = "rate") double rate
    ){
        return courseService.rateCourse(courseId, rate);
    }

    @GetMapping(value = "/like")
    public ResponseResult likeCourse(
            @RequestParam(value = "courseId") String courseId
    ){
        return courseService.likeCourse(courseId);
    }

    @GetMapping(value = "/register")
    public ResponseResult registerCourse(
            @RequestParam(value = "courseId") String courseId
    ){
        return courseService.registerCourse(courseId);
    }

}
