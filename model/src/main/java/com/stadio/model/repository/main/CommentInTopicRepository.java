package com.stadio.model.repository.main;

import com.stadio.model.documents.Comment;
import com.stadio.model.documents.CommentInTopic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentInTopicRepository extends MongoRepository<CommentInTopic, String>  {


    Page<CommentInTopic> findByParentCommentId(String commentId, Pageable pageable);

    Page<CommentInTopic> findByTopicIdAndParentCommentId(String topicId, String parrentId, Pageable pageable);

    long countByParentCommentId(String commentId);

}
