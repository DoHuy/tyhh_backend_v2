package com.stadio.model.repository.main;

import com.stadio.model.documents.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TopicRepository extends MongoRepository<Topic, String> {
}
