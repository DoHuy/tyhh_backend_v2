package com.stadio.model.repository.main;

import com.stadio.model.documents.MDMenu;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MDMenuRepository extends MongoRepository<MDMenu,String> {
}
