package com.stadio.cms.dtos.examOnline;

import lombok.Data;


@Data
public class ExamOnlineTestForm {

    private String id;
    private String startTime;
    //private String endTime;
    private String description;
    private String examCode;
    private Integer price;
    private Integer maximum;
}
