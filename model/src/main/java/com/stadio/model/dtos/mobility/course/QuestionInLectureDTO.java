package com.stadio.model.dtos.mobility.course;

import com.stadio.model.documents.Question;
import com.stadio.model.dtos.mobility.AnswerItemDTO;
import com.stadio.model.dtos.mobility.QuestionItemDTO;
import com.stadio.model.model.Answer;
import lombok.Data;

@Data
public class QuestionInLectureDTO extends QuestionItemDTO {

    private Integer time;

    private String correctAnswer;

    public static QuestionInLectureDTO with(Question q)
    {
        QuestionInLectureDTO questionItem = new QuestionInLectureDTO();

        questionItem.setContent(q.getContent());

        for (Answer answer: q.getAnswers())
        {
            AnswerItemDTO answerItemDTO = new AnswerItemDTO();
            answerItemDTO.setCode(answer.getCode());
            answerItemDTO.setContent(answer.getContent());
            questionItem.getAnswers().add(answerItemDTO);
        }

        questionItem.correctAnswer = q.getCorrectAnswer();
        questionItem.setId(q.getId());

        return questionItem;
    }
}
