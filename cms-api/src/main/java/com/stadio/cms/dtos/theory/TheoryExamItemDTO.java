package com.stadio.cms.dtos.theory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.model.documents.Exam;
import com.stadio.model.enu.ExamType;
import com.stadio.model.enu.TopType;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class TheoryExamItemDTO
{
    private String examId;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "GMT+7")
    private Date createdDate;

    public TheoryExamItemDTO(Exam exam) {
        this.examId = exam.getId();
        this.enable = exam.isEnable();
        this.code = exam.getCode();
        this.name = exam.getName();
        this.time = exam.getTime();
        this.price = exam.getPrice();
        this.clazzId = exam.getClazzId();
        this.type = exam.getType();
        this.createdDate = exam.getCreatedDate();
        this.summary = exam.getSummary();
        this.imageUrl = exam.getImageUrl();
    }
}
