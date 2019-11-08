package com.stadio.model.repository.main;

import com.stadio.model.documents.Chapter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChapterRepository extends MongoRepository<Chapter,String>
{
    Chapter findByCode(String code);
}
