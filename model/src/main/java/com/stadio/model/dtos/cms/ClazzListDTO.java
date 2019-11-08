package com.stadio.model.dtos.cms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.model.documents.Chapter;
import com.stadio.model.documents.Clazz;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ClazzListDTO {
    private String id ;

    private String name;

    private String description;

    private List<String> idChapters;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date createdDate;

    public ClazzListDTO(Clazz clazz) {
        this.id = clazz.getId();
        this.name = clazz.getName();
        this.description = clazz.getDescription();
        this.createdDate = clazz.getCreatedDate();
        this.idChapters = clazz.getIdChapters();
    }
}
