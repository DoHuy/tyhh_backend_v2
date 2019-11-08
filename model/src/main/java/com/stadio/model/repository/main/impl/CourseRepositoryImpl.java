package com.stadio.model.repository.main.impl;

import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Course;
import com.stadio.model.dtos.cms.CourseSearchFormDTO;
import com.stadio.model.repository.main.custom.CourseRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseRepositoryImpl implements CourseRepositoryCustom {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<Course> search(CourseSearchFormDTO courseSearchFormDTO, int page, int pageSize) {
        Query query = new Query();
        if(StringUtils.isValid(courseSearchFormDTO.getName()))
            query.addCriteria(Criteria.where("name").regex(courseSearchFormDTO.getName()));
        if(courseSearchFormDTO.getYear()!=null)
            query.addCriteria(Criteria.where("publishingYear").is(courseSearchFormDTO.getYear()));

        query.addCriteria(Criteria.where("deleted").is(false));
        final Pageable pageableRequest = new PageRequest(page-1, pageSize);
        query.with(pageableRequest);
        return mongoTemplate.find(query,Course.class);
    }

    @Override
    public Long countSearch(CourseSearchFormDTO courseSearchFormDTO) {
        Query query = new Query();
        if(StringUtils.isValid(courseSearchFormDTO.getName()))
            query.addCriteria(Criteria.where("name").regex(courseSearchFormDTO.getName()));
        if(courseSearchFormDTO.getYear()!=null)
            query.addCriteria(Criteria.where("publishingYear").is(courseSearchFormDTO.getYear()));
        query.addCriteria(Criteria.where("deleted").is(false));
        return mongoTemplate.count(query,Course.class);
    }
}
