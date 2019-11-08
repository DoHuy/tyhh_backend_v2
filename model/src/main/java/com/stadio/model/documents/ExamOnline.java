package com.stadio.model.documents;

import com.stadio.model.enu.OnlineTestStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "tab_exam_online")
@Data
public class ExamOnline {

    @Id
    private String id;

    @Field(value = "start_time")
    private Date startTime;

    @Field(value = "end_time")
    private Date endTime;

    @Field(value = "exam_id")
    private String examId;

    @Field(value = "price")
    private Integer price;

    @Field(value = "maximum")
    private Integer maximum;

    @Field(value = "description")
    private String description;

    @Field(value = "status")
    private OnlineTestStatus status;

    @Field(value = "submit_count")
    private int submitCount;

    @Field(value = "average")
    private double average;

    @Field(value = "likes")
    private long likes;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "updated_date")
    private Date updatedDate;

    public ExamOnline() {
        this.updatedDate = new Date();
        this.createdDate = new Date();
    }

    @Override
    public String toString() {
        return "ExamOnline{" +
                "id='" + id + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", examId='" + examId + '\'' +
                ", price=" + price +
                ", maximum=" + maximum +
                ", status=" + status +
                ", submitCount=" + submitCount +
                ", average=" + average +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
