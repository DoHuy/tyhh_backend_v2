package com.stadio.model.repository.main;

import com.stadio.model.documents.Sms;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by Andy on 03/03/2018.
 */
public interface SmsRepository extends MongoRepository<Sms, String>
{
    List<Sms> findByCreatedDateBetween(Date startDate, Date endDate);
}
