package com.stadio.model.repository.main.impl;

import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Question;
import com.stadio.model.repository.main.custom.QuestionRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;

public class QuestionRepositoryImpl implements QuestionRepositoryCustom
{
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Long searchQuestionQuanity(Map questionSearch)
    {
        Query query = new Query();
        if (StringUtils.isValid((String) questionSearch.get("content")))
        {
            query.addCriteria(Criteria.where("content").regex((String) questionSearch.get("content")));
        }
        if (StringUtils.isValid((String) questionSearch.get("chapterId")))
        {
            query.addCriteria(Criteria.where("chapterId").regex((String) questionSearch.get("chapterId")));
        }
        if (StringUtils.isValid((String) questionSearch.get("level")))
        {
            query.addCriteria(Criteria.where("level").regex((String) questionSearch.get("level")));
        }
        if (StringUtils.isValid((String) questionSearch.get("type")))
        {
            query.addCriteria(Criteria.where("type").regex((String) questionSearch.get("type")));
        }
        if (StringUtils.isValid((String) questionSearch.get("clazzId")))
        {
            query.addCriteria(Criteria.where("clazzId").is(questionSearch.get("clazzId")));
        }

        return mongoTemplate.count(query, Question.class);
    }

    @Override
    public List<Question> findQuestionByPage(Integer page, Integer pageSize, Map questionSearch)
    {
        Query query = new Query();
        if (StringUtils.isValid((String) questionSearch.get("content")))
        {
            query.addCriteria(Criteria.where("content").regex((String) questionSearch.get("content")));
        }
        if (StringUtils.isValid((String) questionSearch.get("chapterId")))
        {
            query.addCriteria(Criteria.where("chapterId").is(questionSearch.get("chapterId")));
        }
        if (StringUtils.isValid((String) questionSearch.get("level")))
        {
            query.addCriteria(Criteria.where("level").is(questionSearch.get("level")));
        }
        if (StringUtils.isValid((String) questionSearch.get("type")))
        {
            query.addCriteria(Criteria.where("type").is(questionSearch.get("type")));
        }
        if (StringUtils.isValid((String) questionSearch.get("clazzId")))
        {
            query.addCriteria(Criteria.where("clazzId").is(questionSearch.get("clazzId")));
        }

        final Pageable pageableRequest = new PageRequest(page - 1, pageSize);
        query.with(pageableRequest);
//        query.with(new Sort(Sort.Direction.DESC, "created_date"));

        return mongoTemplate.find(query, Question.class);
    }

    @Override
    public Question randomQuestion(int skip)
    {
//        SampleOperation matchStage = Aggregation.sample(2);
//        Aggregation aggregation = Aggregation.newAggregation(matchStage);
//        AggregationResults<Question> output = mongoTemplate.aggregate(aggregation, "tab_question", Question.class);
//        return output.getMappedResults().get(0);
        Query query = new Query();
        query.skip(skip);
        query.limit(2);
        return mongoTemplate.find(query, Question.class).get(0);
    }

    @Override
    public List<Question> findQuestionByPage(Integer page, Integer pageSize)
    {
        Query query = new Query();
        query.limit(pageSize).skip((page - 1) * pageSize);
        query.with(new Sort(Sort.Direction.DESC, "created_date"));
        return mongoTemplate.find(query, Question.class);
    }
}
