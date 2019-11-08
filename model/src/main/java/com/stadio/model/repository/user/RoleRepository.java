package com.stadio.model.repository.user;

import com.stadio.model.documents.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {

    Role findRoleByName(String name);

}
