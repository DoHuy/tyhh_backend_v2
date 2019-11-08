package com.stadio.model.dtos.cms;

import com.stadio.model.documents.Course;
import lombok.Data;

@Data
public class CourseDetailDTO {
    private String id;
    private String code;
    private String name;
    private Integer price;
    private Integer priceOld;
    private String teacherId;
    private String clazzId;
    private String chapterId;
    private Integer time2pass;
    private String description;
    private String pictureUrl;
    private boolean enable;
    private String videoPreviewUrl;
    private String shareLinkUrl;

    public CourseDetailDTO(Course course){
        this.id = course.getId();
        this.code = course.getCode();
        this.name= course.getName();
        this.price = course.getPrice();
        this.priceOld = course.getPriceOld();
        this.teacherId = course.getTeacherId();
        this.time2pass = course.getTime2pass();
        this.description = course.getDescription();
        this.pictureUrl = course.getPictureUrl();
        this.enable = course.isEnable();
        this.chapterId = course.getChapterId();
        this.clazzId = course.getClazzId();
        this.videoPreviewUrl = course.getVideoPreviewUrl();
        this.shareLinkUrl = course.getShareLinkUrl();
    }
}
