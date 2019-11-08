package com.stadio.model.dtos.mobility;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andy on 02/10/2018.
 */
@Data
public class UserPracticeDTO
{
    private String examId;
    private long remainingTime;
    private Map<String, Object> actions = new HashMap<>();
    private List<QuestionItemDTO> questionList = new ArrayList<>();

}
