package com.stadio.model.repository.main;

import com.stadio.model.documents.UserExamStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserExamStatisticsRepository extends MongoRepository<UserExamStatistics, String> {
    UserExamStatistics findByExamIdRefAndUserIdRef(String examId,String userId);

    UserExamStatistics findFirstByExamIdRef(String examId);
}
