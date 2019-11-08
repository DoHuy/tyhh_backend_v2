package com.stadio.cms.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.model.documents.Exam;
import com.stadio.model.documents.ExamHot;
import com.stadio.model.enu.ExamType;
import com.stadio.model.enu.TopType;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class HotExamItemDTO
{
    private String id;
    private String code;
    private String name;
    private int position;
    private Long time;
    private Integer price;
    private String clazzId;
    private ExamType type;
    private Boolean enable;
    private String summary;
    private int quantity;
    private String imageUrl;
    private TopType topType;

    private Map<String, Object> actions = new HashMap<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date createdDate;

    public static HotExamItemDTO with(Exam exam, ExamHot examHot)
    {
        HotExamItemDTO examItemDTO = new HotExamItemDTO(exam, examHot);
        return examItemDTO;
    }

    public HotExamItemDTO(Exam exam, ExamHot examHot){
        this.id = examHot.getId();
        this.enable = exam.isEnable();
        this.code = exam.getCode();
        this.name = exam.getName();
        this.time = exam.getTime();
        this.price = exam.getPrice();
        this.clazzId = exam.getClazzId();
        this.type = exam.getType();
        this.createdDate = exam.getCreatedDate();
        this.summary = exam.getSummary();
        this.position = examHot.getPosition();
        this.topType = examHot.getTopType();
        this.imageUrl = exam.getImageUrl();
    }
}
