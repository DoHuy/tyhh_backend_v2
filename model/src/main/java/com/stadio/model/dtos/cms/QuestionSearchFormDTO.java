package com.stadio.model.dtos.cms;

import lombok.Data;

@Data
public class QuestionSearchFormDTO {
    private String content;
    private String chapterId;
    private String clazzId;
    private String level;
    private String type;

}
