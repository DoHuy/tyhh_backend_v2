package com.stadio.model.repository.main;

import com.stadio.model.documents.UserCommentAction;
import com.stadio.model.enu.UserCommentActionType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserCommentActionRepository extends MongoRepository<UserCommentAction, String> {

    List<UserCommentAction> findByUserIdRefAndCommentIdRefAndActionIn(String userId, String commentId, List<UserCommentActionType> action, Pageable pageable);

}
