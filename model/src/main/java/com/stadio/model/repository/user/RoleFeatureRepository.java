package com.stadio.model.repository.user;

import com.stadio.model.documents.RoleFeature;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleFeatureRepository extends MongoRepository<RoleFeature, String> {

    void removeByRoleId(String roleId);

    RoleFeature findByRoleIdAndFeatureId(String roleId, String featureId);

    int countByRoleId(String roleId);
}
