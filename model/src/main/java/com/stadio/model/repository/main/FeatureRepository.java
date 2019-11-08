package com.stadio.model.repository.main;

import com.stadio.model.documents.MDFeature;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FeatureRepository extends MongoRepository<MDFeature, String> {

    MDFeature findByHash(String hash);

    List<MDFeature> findByControllerNotNull();

    List<MDFeature> findByFeatureId(String featureId);
}
