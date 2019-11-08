package com.stadio.model.dtos.cms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.model.documents.Question;
import com.stadio.model.model.Answer;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sm on 12/14/17.
 */
@Data
public class QuestionDetailDTO {

    private String id;

    private String content;

    private String type;

    private String clazzId;

    private List<AnswerDTO> answers;

    private String explain;

    private String chapterId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date createDate;

    private Integer position;

    public QuestionDetailDTO(Question question){
        this.id=question.getId();
        this.content=question.getContent();
        this.type =question.getType().toString();
        this.clazzId =question.getClazzId();
        this.createDate = question.getCreatedDate();
        this.answers = mapAnswertoAnswerDTO(question.getAnswers());
        this.explain = question.getExplain();
        this.chapterId = question.getChapterId();
    }

    public List<AnswerDTO> mapAnswertoAnswerDTO(List<Answer> answers){
        List<AnswerDTO> answerDTOS= new ArrayList<>();
        answers.forEach(answer -> {
            answerDTOS.add(new AnswerDTO(answer));
        });
        return answerDTOS;
    }
}
