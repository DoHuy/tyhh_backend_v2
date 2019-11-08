package com.stadio.model.repository.main;

import com.stadio.model.documents.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PurchaseRepository extends MongoRepository<Purchase, String> {
}
