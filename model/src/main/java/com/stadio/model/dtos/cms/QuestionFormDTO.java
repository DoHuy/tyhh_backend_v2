package com.stadio.model.dtos.cms;

import lombok.Data;

import java.util.List;

/**
 * Created by sm on 12/14/17.
 */
@Data
public class QuestionFormDTO {

    private String id;

    private String content;

    private List<AnswerDTO> answers ;

    private String type;

    private String clazzId;

    private String level;

    private String explain;

    private String chapterId;
}
