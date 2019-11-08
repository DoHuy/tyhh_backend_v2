package com.stadio.model.repository.main;

import com.stadio.model.documents.SequenceIndex;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SequenceIndexRepository extends MongoRepository<SequenceIndex, String> {

    public SequenceIndex findFirstBySequenceKey(String sequenceKey);

}
