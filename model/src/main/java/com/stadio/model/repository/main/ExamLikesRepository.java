package com.stadio.model.repository.main;

import com.stadio.model.documents.ExamLikes;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Andy on 03/04/2018.
 */
public interface ExamLikesRepository extends MongoRepository<ExamLikes, String>
{
    ExamLikes findByExamIdAndUserId(String examId, String userId);
}
