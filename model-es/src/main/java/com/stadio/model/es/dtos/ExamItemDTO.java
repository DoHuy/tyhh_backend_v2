package com.stadio.model.es.dtos;

import com.stadio.common.utils.JsonUtils;
import com.stadio.model.es.documents.ESExam;
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

    private Map<String, Object> actions = new HashMap<>();

//    public static ExamItemDTO with(String json) {
//        Exam exam = JsonUtils.parse(json, Exam.class);
//        ExamItemDTO examItemDTO = new ExamItemDTO();
//        examItemDTO.setId(exam.getId());
//        examItemDTO.setName(exam.getName());
//        examItemDTO.setPrice(exam.getPrice());
//        examItemDTO.setTime(exam.getTime());
//        examItemDTO.setImageUrl(exam.getImageUrl());
//        examItemDTO.setQuantity(exam.getQuestionQuantity());
//
//        return examItemDTO;
//    }

    public static ExamItemDTO with(String json) {

        ESExam exam = JsonUtils.parse(json, ESExam.class);

        ExamItemDTO examItemDTO = new ExamItemDTO();
        examItemDTO.setId(exam.getExamId());
        examItemDTO.setName(exam.getName());
        examItemDTO.setPrice(exam.getPrice());
        examItemDTO.setTime(exam.getTime());
        examItemDTO.setImageUrl(exam.getImageUrl());
        examItemDTO.setQuantity(exam.getQuantity());

        return examItemDTO;
    }

    public ExamItemDTO(){}


}
