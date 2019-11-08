package com.stadio.model.repository.main.custom;

import com.stadio.model.documents.Question;

import java.util.List;
import java.util.Map;

public interface QuestionRepositoryCustom {
    Long searchQuestionQuanity(Map questionSearch);

    List<Question> findQuestionByPage(Integer page, Integer pageSize, Map questionSearch);

    Question randomQuestion(int skip);

    List<Question> findQuestionByPage(Integer page, Integer pageSize);
}
