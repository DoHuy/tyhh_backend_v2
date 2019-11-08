package com.stadio.model.redisUtils;

import com.stadio.model.documents.QuestionInExam;
import com.stadio.model.dtos.mobility.QuestionItemDTO;

import java.util.List;
import java.util.Map;

public interface ExamPracticeRedisRepository {
    Map<String, String> processGetExamPractice(String keys);

    List<QuestionItemDTO> processPutExamPractice(String keys, List<QuestionInExam> questionInExamList);
}
