package com.stadio.model.repository.main;

import com.stadio.model.documents.FAQGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FAQGroupRepository extends MongoRepository<FAQGroup, String> {

    List<FAQGroup> findAllByDeletedFalse();
}
