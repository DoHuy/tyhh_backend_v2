package com.stadio.model.repository.main.custom;

import com.stadio.model.documents.Course;
import com.stadio.model.dtos.cms.CourseSearchFormDTO;

import java.util.List;

public interface CourseRepositoryCustom {
    List<Course> search(CourseSearchFormDTO courseSearchFormDTO, int page, int pageSize);

    Long countSearch(CourseSearchFormDTO courseSearchFormDTO);
}
