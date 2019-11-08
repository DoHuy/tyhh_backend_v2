package com.stadio.model.repository.main;

import com.stadio.model.documents.Theory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TheoryRepository extends MongoRepository<Theory, String> {

    Theory findFirstByChapterId(String chapterId);

    List<Theory> findAllByChapterId(String chapterId);

}
