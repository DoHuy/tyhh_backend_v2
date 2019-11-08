package com.stadio.model.redisUtils.impl;

import com.stadio.common.utils.JsonUtils;
import com.stadio.model.documents.Course;
import com.stadio.model.dtos.mobility.CourseItemDTO;
import com.stadio.model.redisUtils.NewestCourseRedisRepository;
import com.stadio.model.redisUtils.RedisConst;
import com.stadio.model.redisUtils.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class NewestCourseRedisRepositoryImpl implements NewestCourseRedisRepository {
    @Autowired
    RedisRepository redisRepository;


    @Override
    public void processPutAllCourse(List<CourseItemDTO> courseItemDTOList) {
        redisRepository.select(RedisConst.DB_NEWEST_COURSE);
        String key = RedisConst.NEWEST_COURSE_HOME;
        courseItemDTOList.forEach(courseItemDTO -> {
            redisRepository.hput(key,courseItemDTO.getId(), JsonUtils.pretty(courseItemDTO));
        });
        redisRepository.expire(key,RedisConst.TIME_TO_LIVE_LONG);
    }

    @Override
    public Map<String, String> processGetCourse() {
        redisRepository.select(RedisConst.DB_NEWEST_COURSE);
        Map<String, String> courseMap = redisRepository.hgetAll(RedisConst.NEWEST_COURSE_HOME);
        return courseMap;
    }

    public void processPutCourses(List<Course> courseList) {
        redisRepository.select(RedisConst.DB_NEWEST_COURSE);
        String key = RedisConst.NEWEST_COURSE_HOME;
        courseList.forEach(courseItem -> {
            redisRepository.hput(key,courseItem.getId(), JsonUtils.pretty(courseItem));
        });
        redisRepository.expire(key,RedisConst.TIME_TO_LIVE_LONG);
    }

    public Map<String, String> processGetCourses() {
        redisRepository.select(RedisConst.DB_NEWEST_COURSE);
        Map<String, String> coursesMap = redisRepository.hgetAll(RedisConst.NEWEST_COURSE_HOME);
        return coursesMap;
    }
}
