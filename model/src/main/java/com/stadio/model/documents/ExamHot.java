package com.stadio.model.documents;

import com.stadio.model.enu.TopType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Data
@Document(collection = "tab_exam_hot", language = "vi")
public class ExamHot implements Serializable
{
    @Id
    private String id;

    @Field(value = "exam_id_ref")
    private String examIdRef;

    @Field(value = "position")
    private Integer position;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "updated_date")
    private Date updatedDate;

    @Field(value = "views")
    private Long views;

    @Field(value = "top_type")
    private TopType topType;

    public ExamHot()
    {
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }
}
