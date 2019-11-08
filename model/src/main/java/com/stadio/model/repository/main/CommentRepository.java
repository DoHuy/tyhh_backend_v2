package com.stadio.model.repository.main;

import com.stadio.model.documents.Comment;
import com.stadio.model.enu.CommentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {

    Page<Comment> findByObjectIdAndType(String objectId, CommentType type, Pageable pageable);

    long countByObjectId(String objectId);
}
