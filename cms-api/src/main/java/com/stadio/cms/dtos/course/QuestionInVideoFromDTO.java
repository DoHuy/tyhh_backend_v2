package com.stadio.cms.dtos.course;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QuestionInVideoFromDTO {

    @NotNull
    private String questionId;

    @NotNull
    private Integer positionInSecond;

}
