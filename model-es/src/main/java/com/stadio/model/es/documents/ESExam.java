package com.stadio.model.es.documents;

import com.stadio.common.utils.JsonUtils;
import com.stadio.model.es.model.Exam;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.Map;

@Data
@Document(indexName = "subjects",type = "exam")
public class ESExam {

    @Id
    private String id;

    @Field(type = FieldType.String)
    private String examId;

    @Field(type = FieldType.String)
    private String name;

    @Field(type = FieldType.String)
    private String imageUrl;

    @Field(type = FieldType.Long)
    private long time;

    @Field(type = FieldType.Integer)
    private int price;

    @Field(type = FieldType.Long)
    private long quantity;

    @Field(type = FieldType.String)
    private String keywords;

    @Field(type = FieldType.Date)
    private Date createdDate;

    @Field(type = FieldType.Boolean)
    private boolean enable;

    @Field(type = FieldType.Boolean)
    private boolean deleted;

    public ESExam() { }

    public static ESExam with(String json)
    {
        Exam exam = JsonUtils.parse(json, Exam.class);

        ESExam examES = new ESExam();

        examES.setExamId(exam.getId());
        examES.setName(exam.getName());
        examES.setPrice(exam.getPrice());
        examES.setTime(exam.getTime());
        examES.setImageUrl(exam.getImageUrl());
        examES.setQuantity(exam.getQuestionQuantity());
        examES.setCreatedDate(exam.getCreatedDate());
        examES.setEnable(exam.isEnable());
        examES.setDeleted(exam.isDeleted());
        examES.setKeywords("");

        if(exam.getKeywords() != null) {
            StringBuilder stringBuilder = new StringBuilder();
            exam.getKeywords().forEach(s -> stringBuilder.append(s + " "));
            examES.setKeywords(stringBuilder.toString());
        }

        return examES;
    }

    public void update(String json) {

        Exam exam = JsonUtils.parse(json, Exam.class);

        this.setExamId(exam.getId());
        this.setName(exam.getName());
        this.setPrice(exam.getPrice());
        this.setTime(exam.getTime());
        this.setImageUrl(exam.getImageUrl());
        this.setQuantity(exam.getQuestionQuantity());
        this.setCreatedDate(exam.getCreatedDate());
        this.setEnable(exam.isEnable());
        this.setDeleted(exam.isDeleted());
        this.setKeywords("");
        if(exam.getKeywords()!=null){
            StringBuilder stringBuilder = new StringBuilder();
            exam.getKeywords().forEach(s -> stringBuilder.append(s + " "));
            this.setKeywords(stringBuilder.toString());
        }
    }

}
