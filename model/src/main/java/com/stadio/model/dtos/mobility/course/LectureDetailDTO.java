package com.stadio.model.dtos.mobility.course;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.model.documents.Lecture;
import com.stadio.model.documents.Paragraph;
import com.stadio.model.dtos.mobility.ExamItemDTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class LectureDetailDTO {

    private String id;

    private String name;

    private String videoUrl;

    private String documentUrl;

    private String content;

    private Boolean isFree;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "GMT+7")
    private Date releaseDate;

    private Boolean isBookmarked;

    private List<ExamItemDTO> exams = new ArrayList<>();

    private List<QuestionInLectureDTO> questions = new ArrayList<>();

    private String topicId;

    public LectureDetailDTO(Lecture lecture){
        id = lecture.getId();
        name = lecture.getName();
        isFree = Boolean.FALSE;
        isBookmarked = Boolean.FALSE;

        videoUrl = lecture.getVideoUrl();
        content = lecture.getContent();
        isFree = lecture.getIsFree();
        documentUrl = lecture.getDocumentUrl();
        this.releaseDate = lecture.getReleaseDate();
        this.topicId = lecture.getTopicId();
    }
}
