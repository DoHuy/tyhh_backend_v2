package com.stadio.model.es.repository;

import com.stadio.model.es.documents.ESExam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ESExamRepository extends ElasticsearchRepository<ESExam,String> {

    ESExam findFirstByExamId(String examId);

    Page<ESExam> findByNameIsLikeOrderByCreatedDate(String text, Pageable pageable);

    Page<ESExam> findByNameMatchesOrderByCreatedDate(String text, Pageable pageable);

    Page<ESExam> findByKeywordsIsLikeAndEnableIsAndDeletedIsOrderByCreatedDate(String text, boolean enable, boolean deleted, Pageable pageable);

    Page<ESExam> findByKeywordsRegexAndEnableIsAndDeletedIsOrderByCreatedDate(String text, boolean enable, boolean deleted, Pageable pageable);
}
