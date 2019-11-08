package com.stadio.mobi.dtos.comment;

import lombok.Data;

import java.util.Date;
import javax.validation.constraints.NotNull;

@Data
public class CommentFormDTO {

    @NotNull
    private String message;

    private Date sendTime;

    private String commentId;

    private String messageLabel;

    private String topicId;

    public CommentFormDTO() {
        this.sendTime = new Date();
    }

}
