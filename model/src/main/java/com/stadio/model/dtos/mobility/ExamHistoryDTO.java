package com.stadio.model.dtos.mobility;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.model.documents.Exam;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 02/13/2018.
 */
@Data
public class ExamHistoryDTO
{
    private String id;
    private String name;
    private long time;
    private long quantity;
    private int correctNumber;
    private long duration;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private Date createdDate;

    private Map<String, Object> actions = new HashMap<>();

    public static ExamHistoryDTO with(Exam exam)
    {
        ExamHistoryDTO examHistoryDTO = new ExamHistoryDTO();

        examHistoryDTO.setId(exam.getId());
        examHistoryDTO.setName(exam.getName());
        examHistoryDTO.setTime(exam.getTime());

        return examHistoryDTO;
    }
}
