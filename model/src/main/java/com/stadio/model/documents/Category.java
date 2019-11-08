package com.stadio.model.documents;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "tab_category")
public class Category implements Serializable
{
    @Id
    private String id;

    @Field(value = "name")
    private String name;

    @Field(value = "summary")
    private String summary;

    @Field(value = "position")
    private int position;

    @Field(value = "image_url")
    private String imageUrl;

    @Field(value = "created_by")
    private String createdBy;

    @Field(value = "updated_by")
    private String updatedBy;

    @DBRef(lazy = true)
    @Field(value = "exam_ids")
    private List<Exam> examIds;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Field(value = "created_date")
    private Date createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Field(value = "updated_date")
    private Date updatedDate;

    @Field(value = "price")
    private long price;

    @Field(value = "price_old")
    private long priceOld;

    @Field(value = "has_correction_detail")
    private Boolean hasCorrectionDetail;

    @Field(value = "teacher_id")
    private String teacherId;

    public Category()
    {
        this.hasCorrectionDetail = Boolean.FALSE;
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }
}
