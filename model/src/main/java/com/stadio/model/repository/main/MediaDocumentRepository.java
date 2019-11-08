package com.stadio.model.repository.main;

import com.stadio.model.documents.MediaDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MediaDocumentRepository extends MongoRepository<MediaDocument, String> {
}
