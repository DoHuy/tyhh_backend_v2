package com.stadio.model.repository.main;

import com.stadio.model.documents.Exam;
import com.stadio.model.repository.main.custom.ExamRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by Andy on 11/10/2017.
 */
public interface ExamRepository extends MongoRepository<Exam, String>, ExamRepositoryCustom
{
    Exam findExamByCode(String code);

    List<Exam> findAllByEnableAndDeleted(Boolean enable, Boolean deleted);

    List<Exam> findAllByHasCorrectionDetailIsNull();

    int countByEnableAndDeleted(boolean enable, boolean deleted);

    @Query("{'enable': true, 'deleted': false}")
    Page<Exam> findExamNewest(Pageable pageable);

    @Query("{'enable': true, 'deleted': false}")
    Page<Exam> findExamOrderBySubmitCountDesc(Pageable pageable);

    @Query("{'enable': true, 'deleted': false, 'chapter_id': ?0}")
    Page<Exam> findExamByChapterIdOrderByCreatedByDesc(String chapterId, Pageable pageable);

    @Query("{'enable': true, 'deleted': false, 'clazz_id_ref': ?0}")
    Page<Exam> findExamByClazzIdOrderByCreatedByDesc(String clazzId, Pageable pageable);

    Page<Exam> findExamByCreatedBy(String createdBy, Pageable pageable);
}
