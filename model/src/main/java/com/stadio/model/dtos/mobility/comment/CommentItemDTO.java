package com.stadio.model.dtos.mobility.comment;

import com.stadio.model.documents.Comment;
import com.stadio.model.documents.CommentInTopic;
import com.stadio.model.enu.CommentType;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentItemDTO {

    private String id;

    private String message;

    private CommentType type;

    private String messageLabel;

    private Date sendTime;

    private String topicId;

    private String avatar;

    private String userId;

    private String userName;

    private List<CommentItemDTO> replies;

    private long repliesTotalCount;

    private long likeTotalCount;

    private Boolean isLiked;

    public CommentItemDTO(CommentInTopic comment) {
        this.id = comment.getId();
        this.message = comment.getMessage();
        this.userId = comment.getUserId();
        this.type = comment.getType();
        this.messageLabel = comment.getMessageLabel().toString();
        this.sendTime = comment.getSendTime();
        this.topicId = comment.getTopicId();
        this.repliesTotalCount = 0;
        this.likeTotalCount = comment.getLikeCount();
        this.isLiked = Boolean.FALSE;
    }
}
