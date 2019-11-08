package com.stadio.cms.dtos.examOnline;

import com.stadio.model.documents.Exam;
import com.stadio.model.documents.ExamOnline;
import com.stadio.model.enu.OnlineTestStatus;
import lombok.Data;

import java.text.SimpleDateFormat;

@Data
public class ExamOnlineItemDTO {

    private String id;
    private String name;
    private String startTime;
    private String endTime;
    private OnlineTestStatus status;
    private int join;

    public static ExamOnlineItemDTO with(ExamOnline examOnline, Exam exam) {
        SimpleDateFormat fm = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        ExamOnlineItemDTO examOnlineItemDTO = new ExamOnlineItemDTO();

        examOnlineItemDTO.setId(examOnline.getId());
        examOnlineItemDTO.setStatus(examOnline.getStatus());
        examOnlineItemDTO.setStartTime(fm.format(examOnline.getStartTime()));
        examOnlineItemDTO.setEndTime(fm.format(examOnline.getEndTime()));
        examOnlineItemDTO.setName(exam.getName());

        return examOnlineItemDTO;
    }

}
