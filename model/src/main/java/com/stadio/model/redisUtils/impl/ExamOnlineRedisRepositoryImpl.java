package com.stadio.model.redisUtils.impl;

import com.stadio.model.documents.Question;
import com.stadio.model.redisUtils.ExamOnlineRedisRepository;
import com.stadio.model.redisUtils.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class ExamOnlineRedisRepositoryImpl implements ExamOnlineRedisRepository {

    @Autowired
    RedisRepository redisRepository;


    //TODO - de lam sau
    private static final String KEY_QUESTION = "question";

    @Override
    public void put(Question question) {
        //String key
        //redisRepository.hmset();
    }

    @Override
    public void putAll(List<Question> questionList) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public Map<String, String> get() {
        return null;
    }
}
