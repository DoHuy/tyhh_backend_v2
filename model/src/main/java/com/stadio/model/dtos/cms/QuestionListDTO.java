package com.stadio.model.dtos.cms;

import com.stadio.model.documents.Question;
import com.stadio.model.enu.QuestionLevel;
import com.stadio.model.enu.QuestionType;
import com.stadio.model.model.Answer;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sm on 12/14/17.
 */
@Data
public class QuestionListDTO {
    private String id;

    private String content;

    private List<AnswerDTO> answers = new ArrayList<>();

    private QuestionType type;

    private String clazzId;

    private QuestionLevel level;

    private String explain;

    private String chapterId;

    public QuestionListDTO(Question question){
        this.id= question.getId();
        this.content = question.getContent();
        this.answers = mapAnswertoAnswerDTO(question.getAnswers());
        this.type = question.getType();
        this.clazzId = question.getClazzId();
        this.level = question.getLevel();
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
