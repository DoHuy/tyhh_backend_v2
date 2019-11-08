package com.stadio.model.repository.main;

import com.stadio.model.documents.FastPractice;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;

/**
 * Created by Andy on 03/02/2018.
 */
public interface FastPracticeRepository extends MongoRepository<FastPractice, String>
{
    int countByUserIdAndCreatedDateBetween(String userId, Date startTime, Date endTime);
}
