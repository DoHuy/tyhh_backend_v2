package com.stadio.mobi.dtos.examOnline;

import com.stadio.model.model.UserAnswer;
import lombok.Data;

import java.util.List;

@Data
public class ExamOnlineSubmitDTO {

    private long duration;
    private long endTime;
    private String examOnlineId;
    private List<UserAnswer> answers;

}