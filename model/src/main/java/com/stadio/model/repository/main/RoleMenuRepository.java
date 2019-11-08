package com.stadio.model.repository.main;

import com.stadio.model.documents.MDMenu;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleMenuRepository extends MongoRepository<MDMenu, String> {

}
