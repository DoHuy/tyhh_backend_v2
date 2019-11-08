package com.stadio.model.repository.main;

import com.stadio.model.documents.UserExam;
import com.stadio.model.enu.PracticeStatus;
import com.stadio.model.repository.main.custom.UserExamRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by Andy on 02/11/2018.
 */
public interface UserExamRepository extends MongoRepository<UserExam, String>, UserExamRepositoryCustom
{
    UserExam findByUserIdRefAndExamIdRef(String userId, String examId);

    List<UserExam> findByUserIdRefOrderByCreatedDateDesc(String userId);

    Long countByExamIdRef(String examId);

    Long countByExamIdRefIsAndCorrectNumberLessThan(String examId,int correctNumber);

    long countByUserIdRefAndExamIdRefIn(String userId, List<String> examIds);

    long countByExamIdRefIn(List<String> examIds);

    List<UserExam> findByCreatedDateBetween(Date startTime, Date endTime);

    List<UserExam> findByExamIdRefAndStatusOrderByCorrectNumberDescDurationAsc(String examIdRef, PracticeStatus status);

    Page<UserExam> findByExamIdRefAndStatusOrderByCorrectNumberDescDurationAsc(String examIdRef, PracticeStatus status, Pageable pageable);

}
