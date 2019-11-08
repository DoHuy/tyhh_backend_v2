package com.stadio.model.documents;

import lombok.Data;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "tab_course")
public class Course {

    @Id
    private String id;

    @Field(value = "code")
    private String code;

    @Field(value = "name")
    private String name;

    @Field(value = "price")
    private Integer price;

    @Field(value = "price_old")
    private Integer priceOld;

    @Field(value = "teacher_id_ref")
    private String teacherId;

    @Field(value = "clazz_id_ref")
    private String clazzId;

    @Field(value = "is_clazz_all")
    private boolean isClazzAll;

    @Field(value = "chapter_id_ref")
    private String chapterId;

    @Field(value = "time_to_pass")
    private Integer time2pass;

    @Field(value = "description")
    private String description;

    @Field(value = "publishing_year")
    private Integer publishingYear;

    @Field(value = "average_rating")
    private Double averageRating;

    @Field(value = "num_votes")
    private Long numVotes;

    @Field(value = "picture_url")
    private String pictureUrl;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "deleted")
    private boolean deleted;

    @Field(value = "enable")
    private boolean enable;

    @Field(value = "keywords")
    private String keywords;

    @Field(value = "votes")
    private List votes;

    @Field(value = "video_preview_url")
    private String videoPreviewUrl;

    @Field(value = "share_link_url")
    private String shareLinkUrl;

    @Field("topic_id")
    @TextIndexed
    private String topicId;

    @Field(value = "release_date")
    private Date releaseDate;

    public Course(){
        this.createdDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.createdDate);
        this.publishingYear = cal.get(Calendar.YEAR);
        this.deleted = false;
        this.enable = false;
        this.numVotes = new Long(0);
        this.averageRating = 0.0;
        this.priceOld = 0;
    }

}
