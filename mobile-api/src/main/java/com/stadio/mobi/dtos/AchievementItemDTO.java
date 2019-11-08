package com.stadio.mobi.dtos;

import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import lombok.Data;

import java.util.Date;

@Data
public class AchievementItemDTO {

    private String id;

    private String name;

    private String thumbnailUrl;

    private String description;

    private Date createdDate;

    private Date updatedDate;

    public AchievementItemDTO(Achievement achievement){
        this.id = achievement.getId();
        this.name = achievement.getName();
        this.thumbnailUrl = achievement.getThumbnailUrl();
        this.description = achievement.getDescription();
        this.createdDate = achievement.getCreatedDate();
        this.updatedDate = achievement.getUpdatedDate();
    }
}
