package com.stadio.model.dtos.cms;

import com.stadio.model.documents.Section;
import lombok.Data;

@Data
public class SectionItemDTO {

    private String id;

    private String name;

    private Long position ;

    private String courseId;

    public SectionItemDTO(Section section){
        this.id= section.getId();
        this.name = section.getName();
        this.position = section.getPosition();
        this.courseId = section.getCourseId();
    }
}
