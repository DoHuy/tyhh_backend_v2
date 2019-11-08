package com.stadio.model.dtos.cms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.model.documents.Exam;
import com.stadio.model.enu.ExamType;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ExamListDTO {

    private String id;
    private String code;
    private String name;
    private List<String> keywords;
    private int position;
    private Long time;
    private Integer price;
    private String clazzId;
    private ExamType type;
    private Boolean enable;
    private String summary;
    private int quantity;
    private String imageUrl;
    private String chapterId;
    private Integer questionMax;
    private Boolean hasCorrectionDetail;

    private Map<String, Object> actions = new HashMap<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date updatedDate;

    private Long views;

    public ExamListDTO(Exam exam)
    {
        this.id = exam.getId();
        this.enable = exam.isEnable();
        this.code = exam.getCode();
        this.name = exam.getName();
        this.time = exam.getTime();
        this.price = exam.getPrice();
        this.clazzId = exam.getClazzId();
        this.type = exam.getType();
        this.createdDate = exam.getCreatedDate();
        this.updatedDate = exam.getUpdatedDate();
        this.summary = exam.getSummary();
        this.views = exam.getViews();
        this.keywords =exam.getKeywords();
        this.position = 0;
        this.imageUrl = exam.getImageUrl();
        this.quantity = exam.getQuestionQuantity();
        this.chapterId = exam.getChapterId();
        this.questionMax = exam.getQuestionMax();
        this.hasCorrectionDetail = exam.getHasCorrectionDetail();
    }

    public ExamListDTO(Exam exam, int position){
        this.id = exam.getId();
        this.enable = exam.isEnable();
        this.code = exam.getCode();
        this.name = exam.getName();
        this.time = exam.getTime();
        this.price = exam.getPrice();
        this.clazzId = exam.getClazzId();
        this.type = exam.getType();
        this.createdDate = exam.getCreatedDate();
        this.updatedDate = exam.getUpdatedDate();
        this.summary = exam.getSummary();
        this.views = exam.getViews();
        this.keywords =exam.getKeywords();
        this.position = position;
        this.imageUrl = exam.getImageUrl();
        this.questionMax = exam.getQuestionMax();
    }

}
