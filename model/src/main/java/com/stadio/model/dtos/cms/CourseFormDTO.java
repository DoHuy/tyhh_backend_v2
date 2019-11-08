package com.stadio.model.dtos.cms;

import lombok.Data;

import java.util.Date;

@Data
public class CourseFormDTO {
    private String id;
    private String code;
    private String name;
    private Integer price;
    private String teacherId;
    private Integer time2pass;
    private String description;
    private String pictureUrl;
    private Boolean enable;
    private String keywords;
    private String clazzId;
    private String chapterId;
    private String videoPreviewUrl;
    private String shareLinkUrl;
    private Date releaseDate;
}
