package com.stadio.mobi.controllers;

import com.stadio.common.utils.ResponseCode;
import com.stadio.mobi.dtos.comment.CommentFormDTO;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.ICommentInTopicService;
import com.stadio.mobi.service.ICommentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private Logger logger = LogManager.getLogger(CommentController.class);

    @Autowired
    ICommentInTopicService commentService;

    @GetMapping(value = "/list")
    public ResponseEntity listCommentsInTopic(
            @RequestParam(value = "topicId") String topicId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize) {
        ResponseResult result = commentService.getListCommentInTopic(topicId, page, pageSize);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/listReplies")
    public ResponseEntity getListReplyInComment(@RequestParam(value = "commentId") String commentId,
                                                @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize) {
        ResponseResult result = commentService.getListReplyInComment(commentId, page, pageSize);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/post")
    public ResponseEntity postComment(
            @RequestBody CommentFormDTO commentForm,
            BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.ok(ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM, null));
        }
        ResponseResult result = commentService.processComment(commentForm);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/reply")
    public ResponseEntity listCommentsInTopic(
            @RequestBody CommentFormDTO commentForm,
            BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.ok(ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM, null));
        }
        ResponseResult result = commentService.processReplyComment(commentForm);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/like")
    public ResponseEntity listCommentsInTopic(
            @RequestParam(value = "commentId") String commentId,
            @RequestParam(value = "unlike") boolean unlike
    ) {
        ResponseResult result = commentService.processLikeComment(commentId, unlike);
        return ResponseEntity.ok(result);
    }

}
