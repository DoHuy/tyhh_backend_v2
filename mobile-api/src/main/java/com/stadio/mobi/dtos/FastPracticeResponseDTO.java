package com.stadio.mobi.dtos;

import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andy on 03/02/2018.
 */
@Data
public class FastPracticeResponseDTO
{
    private String sessionId;
    private List<Question> questionList = new ArrayList<>();
    private Map<String, Object> actions = new HashMap<>();
}
