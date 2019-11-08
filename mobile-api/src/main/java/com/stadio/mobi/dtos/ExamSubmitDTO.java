package com.stadio.mobi.dtos;

import com.stadio.model.model.UserAnswer;
import lombok.Data;

import java.util.List;

@Data
public class ExamSubmitDTO
{
    private long duration;
    private List<UserAnswer> answers;
}
