package com.stadio.model.dtos.mobility;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.common.utils.MathUtils;
import com.stadio.model.documents.Exam;
import com.stadio.model.dtos.cms.ExamDetailDTO;
import com.stadio.model.enu.ExamType;
import com.stadio.model.enu.QuestionType;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andy on 02/08/2018.
 */
@Data
public class ExamDetailsDTO
{
    private String id;
    private String code;
    private String name;
    private List<String> keywords;
    private long time;
    private int price;
    private String clazzId;
    private ExamType type;
    private long quantity;
    private String imageUrl;
    private long views;
    private long like;
    private double average;
    private Boolean isBookmarked;
    private Boolean isLiked;
    private Boolean didDone;
    private Boolean hasCorrectionDetail;
    private String summary;

    private Map<String, String> actions = new HashMap<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date updatedDate;

    public static ExamDetailsDTO with(Exam exam)
    {
        ExamDetailsDTO examDetailsDTO = new ExamDetailsDTO();

        examDetailsDTO.setId(exam.getId());
        examDetailsDTO.setCode(exam.getCode());
        examDetailsDTO.setName(exam.getName());
        examDetailsDTO.setKeywords(exam.getKeywords());
        examDetailsDTO.setTime(exam.getTime());
        examDetailsDTO.setPrice(exam.getPrice());
        examDetailsDTO.setClazzId(exam.getClazzId());
        examDetailsDTO.setType(exam.getType());
        examDetailsDTO.setImageUrl(exam.getImageUrl());
        examDetailsDTO.setLike(exam.getLikes());
        double avg = MathUtils.round(exam.getAverage() * 10.0);
        if (avg <= 5.0) {
            examDetailsDTO.setAverage(avg + 5.0);
        } else {
            examDetailsDTO.setAverage(avg);
        }

        examDetailsDTO.setCreatedDate(exam.getCreatedDate());
        examDetailsDTO.setUpdatedDate(exam.getUpdatedDate());
        examDetailsDTO.setHasCorrectionDetail(exam.getHasCorrectionDetail());
        examDetailsDTO.setSummary(exam.getSummary());
        return examDetailsDTO;
    }

}
