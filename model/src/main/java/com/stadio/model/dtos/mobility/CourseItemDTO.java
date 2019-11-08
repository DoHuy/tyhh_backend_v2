package com.stadio.model.dtos.mobility;

import com.stadio.model.documents.Course;
import lombok.Data;

@Data
public class CourseItemDTO {
    private String id;

    private String name;

    private Integer price;

    private Integer priceOld;

    private String pictureUrl;

    private Double averageRating;

    private Long numVotes;

    private long lectureQuantity;

    private String topicId;

    private Boolean isPurchased;

    public CourseItemDTO(Course course){
        id= course.getId();
        name = course.getName();
        price = course.getPrice();
        priceOld = course.getPriceOld();
        pictureUrl = course.getPictureUrl();
        averageRating = course.getAverageRating();
        numVotes = course.getNumVotes();
        lectureQuantity = 0;
        topicId = course.getTopicId();
        isPurchased = Boolean.FALSE;
    }

    public CourseItemDTO(){}

}
