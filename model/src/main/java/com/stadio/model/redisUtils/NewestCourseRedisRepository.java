package com.stadio.model.redisUtils;

import com.stadio.model.dtos.mobility.CourseItemDTO;

import java.util.List;
import java.util.Map;

public interface NewestCourseRedisRepository {
  void processPutAllCourse(List<CourseItemDTO> courseItemDTOList);

  Map<String, String> processGetCourse();

}
