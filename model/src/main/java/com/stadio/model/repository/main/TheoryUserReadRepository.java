package com.stadio.model.repository.main;

import com.stadio.model.documents.TheoryUserRead;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TheoryUserReadRepository extends MongoRepository<TheoryUserRead, String> {

    TheoryUserRead findFirstByUserIdRefAndAndTheoryIdRef(String userId, String theoryId);

    int countByUserIdRefAndTheoryIdRef(String userId, String theoryId);

}
