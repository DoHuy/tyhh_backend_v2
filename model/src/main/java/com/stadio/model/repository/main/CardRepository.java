package com.stadio.model.repository.main;

import com.stadio.model.documents.Card;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CardRepository extends MongoRepository<Card, String> {
}
