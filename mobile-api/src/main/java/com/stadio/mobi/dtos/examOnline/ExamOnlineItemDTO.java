package com.stadio.mobi.dtos.examOnline;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class ExamOnlineItemDTO {

    private String id;
    private String name;
    private String code;
    private int price;
    private Date startTime;
    private Date endTime;
    private boolean isJoined;
    private boolean isSubmitted;
    private long remainingTime;
    private String examId;

    private Map<String, String> action = new HashMap<>();
}
