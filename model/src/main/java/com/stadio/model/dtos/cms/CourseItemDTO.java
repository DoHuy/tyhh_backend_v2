package com.stadio.model.dtos.cms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.model.documents.Course;
import lombok.Data;

import java.util.Date;

@Data
public class CourseItemDTO {

    private String id;

    private String name;

    private String clazzId;

    private Integer publishingYear;

    private String pictureUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date createdDate;

    private boolean enable;

    private String videoPreviewUrl;

    private Integer price;

    private Date releaseDate;

    public CourseItemDTO(Course course){
        this.id = course.getId();
        this.name =course.getName();
        this.publishingYear = course.getPublishingYear();
        this.pictureUrl = course.getPictureUrl();
        this.createdDate = course.getCreatedDate();
        this.enable = course.isEnable();
        this.clazzId = course.getClazzId();
        this.videoPreviewUrl = course.getVideoPreviewUrl();
        this.price = course.getPrice();
        this.releaseDate = course.getReleaseDate();
    }
}
