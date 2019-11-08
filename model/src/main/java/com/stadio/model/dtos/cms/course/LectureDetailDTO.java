package com.stadio.model.dtos.cms.course;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.model.documents.Lecture;
import com.stadio.model.dtos.mobility.ExamItemDTO;
import com.stadio.model.dtos.mobility.course.QuestionInLectureDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class LectureDetailDTO {

    private String lectureId;

    private String name;

    private String videoUrl;

    private String documentUrl;

    private String content;

    private Boolean isFree;

    private Date releaseDate;

    public LectureDetailDTO(Lecture lecture){
        lectureId = lecture.getId();
        name = lecture.getName();
        isFree = lecture.getIsFree();

        videoUrl = lecture.getVideoUrl();
        content = lecture.getContent();
        isFree = lecture.getIsFree();
        documentUrl = lecture.getDocumentUrl();
        releaseDate = lecture.getReleaseDate();
    }
}
