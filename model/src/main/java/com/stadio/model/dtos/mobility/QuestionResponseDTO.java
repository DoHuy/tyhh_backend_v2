package com.stadio.model.dtos.mobility;

import com.stadio.model.model.Answer;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 02/11/2018.
 */
@Data
public class QuestionResponseDTO
{
    private String id;
    private String content;
    private List<Answer> answers = new ArrayList<>();
    private String correctAnswer;
    private String choose;
    private String explain;

}
