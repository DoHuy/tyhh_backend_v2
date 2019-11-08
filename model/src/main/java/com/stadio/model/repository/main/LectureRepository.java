package com.stadio.model.repository.main;


import com.stadio.model.documents.Lecture;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LectureRepository extends MongoRepository<Lecture, String> {
    List<Lecture> findBySectionIdIsAndDeletedIsOrderByPositionAsc(String sectionId , boolean deleted);

    Long countBySectionIdIs(String sectionId);
}
