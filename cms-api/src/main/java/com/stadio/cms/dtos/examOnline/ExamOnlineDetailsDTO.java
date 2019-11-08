package com.stadio.cms.dtos.examOnline;

import com.stadio.model.documents.Exam;
import com.stadio.model.documents.ExamOnline;
import com.stadio.model.enu.OnlineTestStatus;
import lombok.Data;

import java.text.SimpleDateFormat;

@Data
public class ExamOnlineDetailsDTO {

    private String id;
    private String name;
    private String startTime;
    private String endTime;
    private String description;
    private OnlineTestStatus status;
    private String code;
    private String examId;
    private int join;
    private int price;
    private int maximum;
    private String createdDate;
    private String updatedDate;

    public static ExamOnlineDetailsDTO with(ExamOnline examOnline, Exam exam) {

        SimpleDateFormat fm = new SimpleDateFormat("HH:mm dd/MM/yyyy");

        ExamOnlineDetailsDTO examOnlineDetailsDTO = new ExamOnlineDetailsDTO();

        examOnlineDetailsDTO.setId(examOnline.getId());
        examOnlineDetailsDTO.setName(exam.getName());
        examOnlineDetailsDTO.setStartTime(fm.format(examOnline.getStartTime()));
        examOnlineDetailsDTO.setEndTime(fm.format(examOnline.getEndTime()));
        examOnlineDetailsDTO.setStatus(examOnline.getStatus());
        examOnlineDetailsDTO.setExamId(examOnline.getExamId());
        examOnlineDetailsDTO.setMaximum(examOnline.getMaximum());
        examOnlineDetailsDTO.setPrice(examOnline.getPrice());
        examOnlineDetailsDTO.setCode(exam.getCode());
        examOnlineDetailsDTO.setDescription(examOnline.getDescription());

        examOnlineDetailsDTO.setCreatedDate(fm.format(examOnline.getCreatedDate()));
        examOnlineDetailsDTO.setUpdatedDate(fm.format(examOnline.getUpdatedDate()));

        return examOnlineDetailsDTO;

    }

}
