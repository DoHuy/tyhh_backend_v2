package com.stadio.model.dtos.mobility;

import com.stadio.model.documents.Exam;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 02/08/2018.
 */
@Data
public class ExamItemDTO
{
    private String id;
    private String name;
    private String imageUrl;
    private long time;
    private int price;
    private long quantity;
    private Boolean didDone;
    private Boolean hasCorrectionDetail;

    private Map<String, Object> actions = new HashMap<>();

    public static ExamItemDTO with(Exam exam) {

        ExamItemDTO examItemDTO = new ExamItemDTO();
        examItemDTO.setId(exam.getId());
        examItemDTO.setName(exam.getName());
        examItemDTO.setPrice(exam.getPrice());
        examItemDTO.setTime(exam.getTime());
        examItemDTO.setImageUrl(exam.getImageUrl());
        examItemDTO.setQuantity(exam.getQuestionQuantity());
        examItemDTO.setHasCorrectionDetail(exam.getHasCorrectionDetail());

        return examItemDTO;
    }

    public ExamItemDTO(){}


}
