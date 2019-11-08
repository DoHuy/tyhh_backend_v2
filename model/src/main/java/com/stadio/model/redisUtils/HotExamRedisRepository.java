package com.stadio.model.redisUtils;

import com.stadio.model.documents.Exam;
import com.stadio.model.documents.ExamHot;

import java.util.List;
import java.util.Map;

public interface HotExamRedisRepository {
    void processPutHotExam(ExamHot examHot, Exam exam);

    void processPutAllHotExam(Map<ExamHot,Exam> examHotExamMap,String topType);

    void processDeleteHotExam(ExamHot examHot);

    Map<String,String> processGetHotExam(String topType);
}
