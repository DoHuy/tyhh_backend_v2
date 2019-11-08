package com.stadio.model.repository.main;

import com.stadio.model.documents.FAQ;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FAQRepository extends MongoRepository<FAQ, String> {

    List<FAQ> findFAQSByGroupId(String groupId);

    List<FAQ> findFAQSByGroupIdAndDeletedFalse(String groupId);
}
