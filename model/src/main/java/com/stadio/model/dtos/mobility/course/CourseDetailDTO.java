package com.stadio.model.dtos.mobility.course;

import com.stadio.model.documents.Course;
import com.stadio.model.dtos.mobility.teacher.TeacherDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CourseDetailDTO {

    private String id;

    private String code;

    private String name;

    private Integer price;

    private Integer priceOld;

    private TeacherDTO teacher;

    private String description;

    private Integer publishingYear;

    private Double averageRating;

    private Object votes;

    private Date createdDate;

    private boolean isRegistered;

    private String topicId;

    private String videoPreviewUrl;

    private long likeCount;

    private Boolean isLiked;

    private long bookmarkCount;

    private Boolean isBookmarked;

    private Boolean isRated;

    private String shareLinkUrl;

    public CourseDetailDTO(Course course, TeacherDTO teacher) {
        this.id = course.getId();
        this.code = course.getCode();
        this.name = course.getName();
        this.price = course.getPrice();
        this.priceOld = course.getPriceOld();
        this.description = course.getDescription();
        this.publishingYear = course.getPublishingYear();
        this.averageRating = course.getAverageRating();
        this.createdDate = course.getCreatedDate();

        this.topicId = course.getTopicId();
        this.isLiked = Boolean.FALSE;
        this.isBookmarked = Boolean.FALSE;

        this.teacher = teacher;
        this.videoPreviewUrl = course.getVideoPreviewUrl();

        this.shareLinkUrl = course.getShareLinkUrl();

    }
}
