package com.stadio.model.repository.main;

import com.stadio.model.documents.OnePayRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by Andy on 03/02/2018.
 */
public interface OnePayRequestRepository extends MongoRepository<OnePayRequest, String>
{
    List<OnePayRequest> findByCreatedDateBetween(Date startDate, Date endDate);
}
