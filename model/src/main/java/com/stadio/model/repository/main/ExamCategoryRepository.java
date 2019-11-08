package com.stadio.model.repository.main;

import com.stadio.model.documents.ExamCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExamCategoryRepository extends MongoRepository<ExamCategory, String> {

    Page<ExamCategory> findAllByCategoryId(String categoryId, Pageable pageable);

    List<ExamCategory> findAllByCategoryIdAndExamId(String categoryId, String examId);

}
