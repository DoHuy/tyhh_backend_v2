package com.stadio.model.documents;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stadio.model.enu.ExamType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Andy on 11/10/2017.
 */
@Data
@Document(collection = "tab_exam")
public class Exam implements Serializable
{
    @Id
    private String id;


    @TextIndexed
    @Field(value = "code")
    private String code;

    @TextIndexed
    @Field(value = "name")
    private String name;

    @Field(value = "keywords")
    private List<String> keywords;

    @TextIndexed
    @Field(value = "summary")
    private String summary;

    @Field(value = "time")
    private long time; //second

    @Field(value = "price")
    private int price;

    //@Field(value = "position")
    //private int position;

    @Field(value = "enable")
    private boolean enable;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Field(value = "created_date")
    private Date createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Field(value = "updated_date")
    private Date updatedDate;

    @Field(value = "created_by")
    private String createdBy;

    @Field(value = "updated_by")
    private String updatedBy;

    @Field(value = "clazz_id_ref")
    private String clazzId;

    @Field(value = "type")
    private ExamType type;

    @Field(value = "deleted")
    private boolean deleted;

    @Field(value = "views")
    private Long views;

    @Field(value = "chapter_id")
    private String chapterId;

    @Field(value = "image_url")
    private String imageUrl;

    @Indexed(name = "idx_exam_likes", direction = IndexDirection.DESCENDING)
    @Field(value = "likes")
    private int likes;

    @Indexed(name = "idx_exam_submit_count", direction = IndexDirection.DESCENDING)
    @Field(value = "submit_count")
    private int submitCount;

    @Field(value = "average")
    private double average;

    @Field(value = "question_quantity")
    private int questionQuantity;

    @Field(value = "question_max")
    private int questionMax;

    @Field(value = "has_correction_detail")
    private Boolean hasCorrectionDetail;

//    @DBRef(lazy = true)
//    @Field(value = "category_ids")
//    @JsonIgnore
//    private List<Category> categoryIds;

    public Exam()
    {
        this.createdDate = new Date();
        this.updatedDate = new Date();
        this.deleted = false;
        this.enable = true;
        this.views = Long.valueOf(0);
        this.likes = 0;
        this.questionQuantity = 0;
        this.submitCount = 0;
        this.questionMax = 40;
        this.hasCorrectionDetail = Boolean.FALSE;
    }


}
