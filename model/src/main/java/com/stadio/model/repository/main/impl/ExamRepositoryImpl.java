package com.stadio.model.repository.main.impl;


import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Exam;
import com.stadio.model.repository.main.custom.ExamRepositoryCustom;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by sm on 12/7/17.
 */
@Repository
public class ExamRepositoryImpl implements ExamRepositoryCustom
{

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Long searchExamQuantity(Map examSearch)
    {
        return mongoTemplate.count(this.queryBySearch(examSearch), Exam.class);
    }

    @Override
    public List<Exam> findExamByPage(Integer page, Integer pageSize, Map examSearch)
    {
        Query query = this.queryBySearch(examSearch);

        final Pageable pageableRequest = new PageRequest(page - 1, pageSize);
        query.with(pageableRequest);
        query.with(new Sort(Sort.Direction.DESC, "created_date"));

        return mongoTemplate.find(query, Exam.class);
    }

    private Query queryBySearch(Map examSearch) {
        Query query = new Query();
        if (StringUtils.isValid((String) examSearch.get("code")))
        {
            query.addCriteria(Criteria.where("code").regex((String) examSearch.get("code")));
        }
        if (StringUtils.isValid((String) examSearch.get("name")))
        {
            query.addCriteria(Criteria.where("name").regex((String) examSearch.get("name")));
        }
        if(StringUtils.isValid((String) examSearch.get("clazzId"))){
            query.addCriteria(Criteria.where("clazzId").is(examSearch.get("clazzId")));
        }
        if (StringUtils.isValid((String) examSearch.get("type")))
        {
            query.addCriteria(Criteria.where("type").is(examSearch.get("type")));
        }
        if (StringUtils.isValid((String) examSearch.get("createdBy")))
        {
            query.addCriteria(Criteria.where("created_by").is(((String) examSearch.get("createdBy")).trim()));
        }

        Boolean hasCorrectionDetail = (Boolean) examSearch.get("hasCorrectionDetail");
        if (hasCorrectionDetail != null) {
            query.addCriteria(Criteria.where("has_correction_detail").is(hasCorrectionDetail));
        }
        query.addCriteria(Criteria.where("deleted").is(false));
        return query;
    }

    @Override
    public List<Exam> findExamByPage(Integer page, Integer pageSize)
    {
        Query query = new Query();

        query.limit(pageSize).skip((page - 1) * pageSize);

        query.with(new Sort(Sort.Direction.DESC, "created_date"));

        return mongoTemplate.find(query, Exam.class);
    }

    @Override
    public List<Exam> fullTextSearch(String q, int limit, int offset)
    {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(q);
        Query query = TextQuery.queryText(criteria);

        return mongoTemplate.find(query, Exam.class);
    }

}
