package com.stadio.model.repository.main;

import com.stadio.model.documents.ExamSubscribe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExamSubscribeRepository extends MongoRepository<ExamSubscribe, String> {

    int countByExamId(String examId);

    Page<ExamSubscribe> findByExamId(String examId, Pageable pageable);

    List<ExamSubscribe> findAllByExamId(String examId);

    ExamSubscribe findByExamIdAndUserId(String examId, String userId);

}
