package com.stadio.model.repository.main;

import com.stadio.model.documents.Exam;
import com.stadio.model.documents.QuestionInExam;
import com.stadio.model.repository.main.custom.QuestionInExamCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionInExamRepository extends MongoRepository<QuestionInExam, String>, QuestionInExamCustom
{
    QuestionInExam findFirstByExamAndPosition(Exam exam, int position);
}
