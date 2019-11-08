package com.stadio.task.baker.exam;

import com.stadio.model.documents.Exam;
import com.stadio.model.enu.ExamType;

import java.util.ArrayList;
import java.util.List;

public class ExamGenerator {

    public static  Exam generateExam(int i,String examId){
        Exam exam = new Exam();
        exam.setCode("EXAMTEST2018"+i);
        exam.setName("Đề thi thử số "+i);
        exam.setType(ExamType.ALL);
        exam.setClazzId(examId);
        List keywords = new ArrayList();
        keywords.add("de thi thu");
        keywords.add("2018");
        exam.setKeywords(keywords);
        exam.setTime(60);
        exam.setPrice(0);
        exam.setEnable(true);
        return exam;
    }
}
