package com.stadio.model.dtos.cms;

import com.stadio.model.documents.Exam;
import lombok.Data;

@Data
public class LectureExamItemDTO {
    private String id;
    private String code;
    private String name;

    public LectureExamItemDTO(Exam exam){
        id= exam.getId();
        code= exam.getCode();
        name = exam.getName();

    }
}
