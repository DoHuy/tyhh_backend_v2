package com.stadio.model.redisUtils;

import com.stadio.model.documents.Question;

import java.util.List;
import java.util.Map;

public interface ExamOnlineRedisRepository {

    void put(Question question);

    void putAll(List<Question> questionList);

    void delete(String id);

    Map<String, String> get();

}
