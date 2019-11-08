package com.stadio.model.repository.user;

import com.stadio.model.documents.ManagerRole;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface ManagerRoleRepository extends MongoRepository<ManagerRole, String> {

    List<ManagerRole> findByManagerId(String managerId);

    ManagerRole findByManagerIdAndRoleId(String managerId, String roleId);

    List<ManagerRole> findByRoleId(String roleId);

    void removeByManagerId(String managerId);
}
