package com.stadio.mobi.service.impl;

import com.hoc68.users.documents.User;
import com.stadio.common.utils.HelperUtils;
import com.stadio.common.utils.ResponseCode;
import com.stadio.mobi.dtos.comment.CommentFormDTO;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.ICommentInTopicService;
import com.stadio.model.documents.Comment;
import com.stadio.model.documents.CommentInTopic;
import com.stadio.model.documents.Topic;
import com.stadio.model.documents.UserCommentAction;
import com.stadio.model.dtos.mobility.comment.CommentItemDTO;
import com.stadio.model.enu.UserCommentActionType;
import com.stadio.model.repository.main.CommentInTopicRepository;
import com.stadio.model.repository.main.TopicRepository;
import com.stadio.model.repository.main.UserCommentActionRepository;
import com.stadio.model.repository.user.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class CommentInTopicService extends BaseService  implements ICommentInTopicService {

    @Autowired
    CommentInTopicRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    UserCommentActionRepository userCommentActionRepository;

    private Logger logger = LogManager.getLogger(CommentService.class);

    @Override
    public ResponseResult getListCommentInTopic(String topicId, int page, int pageSize) {
        if (topicRepository.findOne(topicId) == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("topic.not.found"));
        }

        PageRequest request = new PageRequest(page - 1, pageSize, new Sort(Sort.Direction.DESC, "created_date"));
        List<CommentInTopic> commentList = commentRepository.findByTopicIdAndParentCommentId(topicId, null, request).getContent();

        List<CommentItemDTO> commentItemDTOS = new ArrayList<>();
        for (CommentInTopic comment: commentList) {
            CommentItemDTO commentItemDTO = this.getCommentDTO(comment);
            commentItemDTOS.add(commentItemDTO);
        }
        return ResponseResult.newSuccessInstance(commentItemDTOS);
    }

    @Override
    public ResponseResult getListReplyInComment(String commentId, int page, int pageSize) {
        CommentInTopic comment = commentRepository.findOne(commentId);
        if (comment == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("comment.not.found"));
        }

        if (topicRepository.findOne(comment.getTopicId()) == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("topic.not.found"));
        }

        if (!comment.isHasReplies()) {
            return ResponseResult.newSuccessInstance(new ArrayList<>());
        }

        List<CommentItemDTO> replyItemDTOs = new ArrayList<>();

        //TODO get most like comment
        PageRequest request = new PageRequest(page - 1, pageSize, new Sort(Sort.Direction.DESC, "created_date"));

        List<CommentInTopic> replyList = commentRepository.findByParentCommentId(commentId, request).getContent();

        for (CommentInTopic reply: replyList) {
            CommentItemDTO replyItemDTO = this.getCommentDTO(reply);
            replyItemDTOs.add(replyItemDTO);
        }
        return ResponseResult.newSuccessInstance(replyItemDTOs);
    }

    @Override
    public Topic processCreateTopic(String className) {
        Topic topic = new Topic();
        topic.setClassObj(className);
        topicRepository.save(topic);
        return topic;
    }

    @Override
    public ResponseResult processComment(CommentFormDTO commentForm) {
        //TODO validate comment xss,sqli,...
        //TODO Block spam

        if (topicRepository.findOne(commentForm.getTopicId()) == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("topic.not.found"));
        }

        CommentInTopic comment = new CommentInTopic();
        comment.setMessage(commentForm.getMessage());
        comment.setMessageLabel(commentForm.getMessageLabel());
        comment.setUserId(getUserRequesting().getId());
        comment.setSendTime(new Date());
        comment.setTopicId(commentForm.getTopicId());

        commentRepository.save(comment);

        return ResponseResult.newSuccessInstance(getCommentDTO(comment));
    }

    @Override
    public ResponseResult processReplyComment(CommentFormDTO commentForm) {
        CommentInTopic comment = commentRepository.findOne(commentForm.getCommentId());
        if (comment == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("comment.not.found"));
        }

        if (topicRepository.findOne(comment.getTopicId()) == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("topic.not.found"));
        }

        //TODO validate comment xss,sqli,...
        CommentInTopic reply = new CommentInTopic();
        reply.setUserId(getUserRequesting().getId());
        reply.setSendTime(new Date());
        reply.setMessage(commentForm.getMessage());
        reply.setMessageLabel(commentForm.getMessageLabel());
        reply.setTopicId(comment.getTopicId());
        reply.setParentCommentId(comment.getId());
        commentRepository.save(reply);

        if (!comment.isHasReplies()) {
            comment.setHasReplies(true);
            commentRepository.save(comment);
        }

        //TODO push notification to user here

        return ResponseResult.newSuccessInstance(getCommentDTO(comment));
    }

    private CommentItemDTO getCommentDTO(CommentInTopic comment) {
        CommentItemDTO commentItemDTO = new CommentItemDTO(comment);
        commentItemDTO.setUserId(comment.getUserId());

        try {
            if (comment.isHasReplies()) {
                //If have reply
                List<CommentItemDTO> replyItemDTOs = new ArrayList<>();

                //TODO Get most like reply first
                PageRequest request = new PageRequest(0, 2, new Sort(Sort.Direction.DESC, "created_date"));
                List<CommentInTopic> replies = commentRepository.findByParentCommentId(comment.getId(), request).getContent();


                for (CommentInTopic reply: replies) {
                    CommentItemDTO replyItemDTO = this.getCommentDTO(reply);
                    replyItemDTOs.add(replyItemDTO);
                }
                commentItemDTO.setReplies(replyItemDTOs);
                commentItemDTO.setRepliesTotalCount(commentRepository.countByParentCommentId(comment.getId()));
            }
        } catch (Exception e) { logger.error("comment load replies error:", comment.getId()); }

        try {
            User user = userRepository.findOne(comment.getUserId());

            commentItemDTO.setUserName(user.getFullName());
            commentItemDTO.setUserId(user.getId());
            commentItemDTO.setAvatar(user.getAvatar());

        } catch (Exception e) {logger.error("comment not found user", comment.getUserId());}

        try {
             commentItemDTO.setIsLiked(isUserLikedComment(comment.getId()));
        } catch (Exception e) { logger.error("comment load replies error:", comment.getId()); }


        return commentItemDTO;
    }

    private boolean isUserLikedComment(String commentId) {
        PageRequest request = new PageRequest(0, 2, new Sort(Sort.Direction.DESC, "created_date"));
        List<UserCommentAction> userCommentActions =
                userCommentActionRepository.findByUserIdRefAndCommentIdRefAndActionIn(
                        getUserRequesting().getId(),
                        commentId,
                        Arrays.asList(UserCommentActionType.LIKE, UserCommentActionType.UNLIKE),
                        request
                );
        if (HelperUtils.isEmptyArray(userCommentActions)) {
            return false;
        }
        if ( userCommentActions.get(0).getAction() == UserCommentActionType.LIKE) {
            return true;
        }
        return false;
    }

    public ResponseResult processLikeComment(String commentId, boolean unlike) {
        CommentInTopic comment = commentRepository.findOne(commentId);
        if (comment == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("comment.not.found"));
        }

        PageRequest request = new PageRequest(0, 2, new Sort(Sort.Direction.DESC, "created_date"));

        List<UserCommentAction> userCommentActions =
                userCommentActionRepository.findByUserIdRefAndCommentIdRefAndActionIn(
                        getUserRequesting().getId(),
                        commentId,
                        Arrays.asList(UserCommentActionType.LIKE, UserCommentActionType.UNLIKE),
                        request
                );

        if (unlike == false ) {
            if (HelperUtils.isEmptyArray(userCommentActions) ||
                    (userCommentActions.size() > 0 && userCommentActions.get(0).getAction() == UserCommentActionType.UNLIKE)) {
                UserCommentAction action = new UserCommentAction();
                action.setUserIdRef(getUserRequesting().getId());
                action.setCommentIdRef(commentId);
                action.setAction(UserCommentActionType.LIKE);

                userCommentActionRepository.save(action);
                comment.setLikeCount(comment.getLikeCount() + 1);
            } else {
                return ResponseResult.newSuccessInstance(null);
            }
        } else {
            if (!HelperUtils.isEmptyArray(userCommentActions) && userCommentActions.get(0).getAction() == UserCommentActionType.LIKE) {
                UserCommentAction action = new UserCommentAction();
                action.setUserIdRef(getUserRequesting().getId());
                action.setCommentIdRef(commentId);
                action.setAction(UserCommentActionType.UNLIKE);

                userCommentActionRepository.save(action);

                comment.setLikeCount(comment.getLikeCount() - 1);
            } else {
                return ResponseResult.newSuccessInstance(null);
            }
        }

        commentRepository.save(comment);
        return ResponseResult.newSuccessInstance(null);
        //TODO Noti user
    }

}
