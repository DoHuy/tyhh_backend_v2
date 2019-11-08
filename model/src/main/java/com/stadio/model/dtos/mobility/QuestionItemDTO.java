package com.stadio.model.dtos.mobility;

import com.stadio.common.utils.JsonUtils;
import com.stadio.model.documents.Question;
import com.stadio.model.model.Answer;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 02/07/2018.
 */
@Data
public class QuestionItemDTO
{
    private String id;
    private String content;
    private List<AnswerItemDTO> answers = new ArrayList<>();

    public static QuestionItemDTO with(Question q)
    {
        QuestionItemDTO questionItem = new QuestionItemDTO();

        questionItem.setContent(q.getContent());

        for (Answer answer: q.getAnswers())
        {
            AnswerItemDTO answerItemDTO = new AnswerItemDTO();
            answerItemDTO.setCode(answer.getCode());
            answerItemDTO.setContent(answer.getContent());
            questionItem.getAnswers().add(answerItemDTO);
        }
        questionItem.setId(q.getId());

        return questionItem;
    }

    public static QuestionItemDTO with(String json)
    {
        try {
            QuestionItemDTO questionItem = JsonUtils.convertJsonToObject(json, QuestionItemDTO.class);
            return questionItem;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
