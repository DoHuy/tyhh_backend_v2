package com.stadio.model.repository.main.custom;

import com.stadio.model.documents.Question;
import com.stadio.model.documents.QuestionInExam;

import java.util.List;

public interface QuestionInExamCustom {
    QuestionInExam getQuestionNthOfExam(String id, Integer nth);

    Long getQuestionQuantityOfExam(String id);

    List<QuestionInExam> getQuestionOfExam(String id);

    Boolean checkQuestionUse(Question question);
}
