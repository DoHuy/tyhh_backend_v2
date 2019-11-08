package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "tab_exam_subscribe")
public class ExamSubscribe {

    @Id
    private String id;

    @Field(value = "exam_id")
    private String examId;

    @Field(value = "user_id")
    private String userId;

    @Field(value = "join_time")
    private Date joinTime;

    @Field(value = "receive_message")
    private boolean receiveMessage;

    @Field(value = "receive_email")
    private boolean receiveEmail;

    @Field(value = "created_date")
    private Date createdDate;

    public ExamSubscribe() {
        this.joinTime = new Date();
        this.createdDate = new Date();
    }

}
