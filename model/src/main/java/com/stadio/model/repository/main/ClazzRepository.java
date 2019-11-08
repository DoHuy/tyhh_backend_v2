package com.stadio.model.repository.main;

import com.stadio.model.documents.Clazz;
import com.stadio.model.repository.main.custom.ClazzRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClazzRepository extends MongoRepository<Clazz,String>, ClazzRepositoryCustom {
}
