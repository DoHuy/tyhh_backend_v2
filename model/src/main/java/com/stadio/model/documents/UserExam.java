package com.stadio.model.documents;

import com.stadio.model.enu.PracticeStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Andy on 02/10/2018.
 */
@Data
@Document(collection = "tab_user_exam")
@CompoundIndexes({
        @CompoundIndex(name = "user_created_date", def = "{'user_id':1, 'created_date': -1}"),
        @CompoundIndex(name = "exam_created_date", def = "{'exam_id':1, 'created_date': -1}"),
        @CompoundIndex(name = "idx_created_date", def = "{'created_date': -1}")
})
public class UserExam
{
    @Id
    private String id;

    @Field(value = "exam_id_ref")
    private String examIdRef;

    @Field(value = "user_id")
    private String userIdRef;

    @Field(value = "correct_number")
    private int correctNumber;

    @Field(value = "total")
    private int total;

    @Field(value = "start_time")
    private Date startTime;

    @Field(value = "end_time")
    private Date endTime;

    @Field(value = "status")
    private PracticeStatus status;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "updated_date")
    private Date updatedDate;

    @Field(value = "details")
    private String details;

    @Field(value = "duration")
    private long duration;

    public UserExam() {
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }
}
