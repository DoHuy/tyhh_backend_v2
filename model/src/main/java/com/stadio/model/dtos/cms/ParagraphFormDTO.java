package com.stadio.model.dtos.cms;

import lombok.Data;

import java.util.Date;

@Data
public class ParagraphFormDTO {
    private String id;

    private String name;

    private String videoUrl;

    private String documentUrl;

    private Boolean isFree;

    private Date releaseDate;

    private String content;

    private String lectureId;

    private String sectionId;

    public ParagraphFormDTO() {
        this.isFree = Boolean.FALSE;
    }
}
