package com.stadio.task.model;

import com.stadio.model.model.UserAnswer;
import lombok.Data;

import java.util.List;

@Data
public class ExamOnlineSubmitDTO {

    private long duration;
    private String senderId;
    private String examOnlineId;
    private List<UserAnswer> answers;

}