package com.stadio.model.dtos.cms;

import com.stadio.model.documents.Chapter;
import lombok.Data;

@Data
public class ChapterFormDTO {

    private String id ;
    private String name;
    private String code;
    private String description;

    public ChapterFormDTO(Chapter chapter) {
        this.id = chapter.getId();
        this.code = chapter.getCode();
        this.name = chapter.getName();
        this.description = chapter.getDescription();
    }
}
