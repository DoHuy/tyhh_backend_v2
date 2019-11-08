package com.stadio.model.dtos.mobility;

import com.stadio.model.documents.Exam;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ExamSearchDTO {
    private String id;
    private String name;
    private String imageUrl;
    private long time;
    private int price;
    private long quantity;
    private long views;

    public static ExamSearchDTO with(Exam exam)
    {
        ExamSearchDTO examSearchDTO = new ExamSearchDTO();
        examSearchDTO.setId(exam.getId());
        examSearchDTO.setName(exam.getName());
        examSearchDTO.setPrice(exam.getPrice());
        examSearchDTO.setTime(exam.getTime());
        examSearchDTO.setViews(exam.getViews());
        examSearchDTO.setImageUrl(exam.getImageUrl());
        return examSearchDTO;
    }
}
