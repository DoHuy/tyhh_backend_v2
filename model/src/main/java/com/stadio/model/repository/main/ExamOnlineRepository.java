package com.stadio.model.repository.main;

import com.stadio.model.documents.ExamOnline;
import com.stadio.model.enu.OnlineTestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ExamOnlineRepository extends MongoRepository<ExamOnline, String> {

    @Query("{status: {{$ne: 'CANCELLED'}}")
    ExamOnline findByExamId(String id);

    Page<ExamOnline> findByStatusNot(OnlineTestStatus status, Pageable pageable);

    Page<ExamOnline> findByStatus(OnlineTestStatus status, Pageable pageable);

    List<ExamOnline> findByStatusOrderByCreatedDateDesc(OnlineTestStatus status);
}
