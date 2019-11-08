package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by Andy on 03/04/2018.
 */
@Data
@Document(collection = "tab_exam_likes")
public class ExamLikes
{
    @Id
    private String id;

    @Field(value = "exam_id")
    private String examId;

    @Field(value = "user_id")
    private String userId;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "updated_date")
    private Date updatedDate;

    public ExamLikes()
    {
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }
}
