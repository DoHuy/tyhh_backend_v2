package com.stadio.model.repository.main;

import com.stadio.model.documents.Section;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SectionRepository extends MongoRepository<Section, String> {
    List<Section> findByCourseIdIsAndDeletedIsOrderByPositionAsc(String courseId,Boolean deleted);

    Long countByCourseIdIs(String courseId);
}
