package com.stadio.model.repository.main;

import com.stadio.model.documents.GoogleOrder;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GooglePayloadRepository extends MongoRepository<GoogleOrder, String> {

    GoogleOrder findByPayload(String payload);

}
