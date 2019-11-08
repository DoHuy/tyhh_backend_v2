package com.stadio.model.repository.chemistry;

import com.stadio.model.documents.chemistry.PeriodicTable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PeriodicTableRepository extends MongoRepository<PeriodicTable, String> {
}
