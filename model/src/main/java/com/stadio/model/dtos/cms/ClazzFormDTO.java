package com.stadio.model.dtos.cms;

import lombok.Data;

import java.util.List;

@Data
public class ClazzFormDTO {
    private String id ;

    private String name;

    private String description;

    private List<String> idChapters;
}
