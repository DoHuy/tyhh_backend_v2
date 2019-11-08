package com.stadio.model.dtos.cms;

import com.stadio.model.documents.Lecture;
import lombok.Data;

import java.util.Date;

@Data
public class LectureItemDTO {

    private String id;

    private String name;

    private Long position;

    public LectureItemDTO(Lecture lecture){
        id = lecture.getId();
        name = lecture.getName();
        position = lecture.getPosition();
    }
}
