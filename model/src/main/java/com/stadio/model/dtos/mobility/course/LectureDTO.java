package com.stadio.model.dtos.mobility.course;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.model.documents.Lecture;
import com.stadio.model.documents.Paragraph;
import lombok.Data;

import java.util.Date;

@Data
public class LectureDTO {

    private String id;

    private String name;

    private int examCount;

    private int examSubmittedCount;

    private int videoDuration;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "GMT+7")
    private Date releaseDate;

    private Boolean isFree;

    private Boolean isPurchased;

    private String topicId;

    public LectureDTO(Lecture lecture) {
        this.id = lecture.getId();
        this.name = lecture.getName();
        this.isFree = Boolean.FALSE;
        this.isPurchased = Boolean.FALSE;
        if (lecture.getExamList() != null) {
            this.examCount = lecture.getExamList().size();
        }
        this.isFree = lecture.getIsFree();
        this.videoDuration = lecture.getVideoDuration();
        this.releaseDate = lecture.getReleaseDate();
        this.topicId = lecture.getTopicId();
    }
}
