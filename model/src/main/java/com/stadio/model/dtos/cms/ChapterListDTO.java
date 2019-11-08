package com.stadio.model.dtos.cms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.model.documents.Chapter;
import lombok.Data;

import java.util.Date;

@Data
public class ChapterListDTO {

    private String id ;
    private String name;
    private String code;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date createdDate;

    public ChapterListDTO(Chapter chapter){
        this.id= chapter.getId();
        this.name = chapter.getName();
        this.description = chapter.getDescription();
        this.createdDate = chapter.getCreatedDate();
        this.code = chapter.getCode();
    }
}
