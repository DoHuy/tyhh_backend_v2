package com.stadio.mobi.service;

import com.stadio.mobi.dtos.comment.CommentFormDTO;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.model.documents.Topic;

public interface ICommentService {

    ResponseResult getListCommentFromExamOnline(String id, int page, int limit);

    ResponseResult processSendingMessage(String id, String msg);

}
