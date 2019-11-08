package com.stadio.mobi.service;

import com.stadio.mobi.dtos.comment.CommentFormDTO;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.model.documents.Comment;
import com.stadio.model.documents.Topic;
import com.stadio.model.dtos.mobility.comment.CommentItemDTO;

public interface ICommentInTopicService {

    ResponseResult getListReplyInComment(String commentId, int page, int pageSize);

    ResponseResult getListCommentInTopic(String topicId, int page, int pageSize);

    Topic processCreateTopic(String className);

    ResponseResult processComment(CommentFormDTO commentForm);

    ResponseResult processReplyComment(CommentFormDTO commentForm);

    ResponseResult processLikeComment(String commentId, boolean unlike);

}
