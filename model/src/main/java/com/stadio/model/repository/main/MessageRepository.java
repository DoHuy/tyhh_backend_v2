package com.stadio.model.repository.main;

import com.stadio.model.documents.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Andy on 03/04/2018.
 */
public interface MessageRepository extends MongoRepository<Message, String>
{
    List<Message> findMessagesByUserIdOrderByCreatedDateDesc(String userId);
}
