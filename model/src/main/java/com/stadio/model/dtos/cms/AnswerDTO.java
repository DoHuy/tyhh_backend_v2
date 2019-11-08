package com.stadio.model.dtos.cms;

import com.stadio.model.model.Answer;
import lombok.Data;

@Data
public class AnswerDTO
{
    private String code;

    private String content;

    private Integer isCorrect;

    public AnswerDTO(Answer answer) {
        this.code = answer.getCode();
        this.content = answer.getContent();
        this.isCorrect = answer.isCorrect()? 1 : 0;
    }

    public AnswerDTO() {
    }
}