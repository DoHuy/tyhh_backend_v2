package com.stadio.model.model.course;

import lombok.Data;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;

@Data
public class QuestionInVideo implements Comparable {

    @NotNull
    @Field("question_id")
    @TextIndexed
    private String questionId;

    @NotNull
    @Field("position_in_second")
    private Integer positionInSecond;

    public QuestionInVideo(String questionId, Integer positionInSecond) {
        this.questionId = questionId;
        this.positionInSecond = positionInSecond;
    }

    @Override
    public int compareTo(Object o) {
        QuestionInVideo question = (QuestionInVideo) o;
        return this.positionInSecond - question.getPositionInSecond();
    }
}
