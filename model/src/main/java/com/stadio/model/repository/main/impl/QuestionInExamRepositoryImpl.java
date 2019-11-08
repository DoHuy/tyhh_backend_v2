package com.stadio.model.repository.main.impl;

import com.stadio.model.documents.Question;
import com.stadio.model.documents.QuestionInExam;
import com.stadio.model.repository.main.custom.QuestionInExamCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class QuestionInExamRepositoryImpl implements QuestionInExamCustom {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public QuestionInExam getQuestionNthOfExam(String id, Integer nth) {
        Query query = new Query();
        query.addCriteria(Criteria.where("exam").is(id));
        query.addCriteria(Criteria.where("position").is(nth));
        QuestionInExam questionInExam = mongoTemplate.findOne(query, QuestionInExam.class);
        return questionInExam;
    }

    @Override
    public Long getQuestionQuantityOfExam(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("exam").is(id));
        Long quantity = mongoTemplate.count(query, QuestionInExam.class);
        return quantity>50 ? 50 : quantity;
    }

    @Override
    public List<QuestionInExam> getQuestionOfExam(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("exam").is(id));
        query.with(new Sort(Sort.Direction.ASC, "position"));
        List<QuestionInExam> questionInExams = mongoTemplate.find(query,QuestionInExam.class);
        return questionInExams;
    }

    @Override
    public Boolean checkQuestionUse(Question question) {
        Query query = new Query();
        query.addCriteria(Criteria.where("question").is(question.getId()));

        List<QuestionInExam> questionInExams = mongoTemplate.find(query,QuestionInExam.class);
        if(questionInExams==null)
            return false;
        for(int pos =0;pos<questionInExams.size();pos++){
            if(!questionInExams.get(pos).getExam().isDeleted())
                return true;
        }
        return false;
    }
}
