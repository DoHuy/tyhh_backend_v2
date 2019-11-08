package com.stadio.mobi.dtos.examOnline;

import lombok.Data;

import java.util.Date;

@Data
public class ExamCommentDTO {

    private String id;
    private String name;
    private String avatar;
    private String message;
    private Date sendTime;
    private String messageLabel;
}
