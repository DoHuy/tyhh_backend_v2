package com.stadio.model.dtos;

import com.stadio.model.documents.Lecture;
import com.stadio.model.documents.Paragraph;
import lombok.Data;

@Data
public class ParagraphItemDTO {

    private String id;

    private String name;

    private String videoUrl;

    private String documentUrl;

    private String content;

    private String lectureId;

    private Long position;

    private Boolean isFree;

    public ParagraphItemDTO(Lecture lecture){
        name= lecture.getName();
        videoUrl = lecture.getVideoUrl();
        content = lecture.getContent();
        lectureId = lecture.getId();
        position = lecture.getPosition();
        isFree = lecture.getIsFree();
        documentUrl = lecture.getDocumentUrl();
    }
}
