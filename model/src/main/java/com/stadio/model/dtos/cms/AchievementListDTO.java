package com.stadio.model.dtos.cms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.model.documents.Achievement;
import com.stadio.model.enu.RankType;
import com.stadio.model.enu.ScheduleType;
import lombok.Data;

import java.util.Date;

@Data
public class AchievementListDTO {
    private String id;

    private String name;

    private String thumbnailUrl;

    private String description;

    private String condition;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date createdDate;

    private ScheduleType scheduleType;



    public AchievementListDTO(Achievement achievement) {
        this.id = achievement.getId();
        this.name = achievement.getName();
        this.thumbnailUrl = achievement.getThumbnailUrl();
        this.description = achievement.getDescription();
        this.condition = achievement.getCondition();
        this.createdDate = achievement.getCreatedDate();
        this.scheduleType = achievement.getScheduleType();
    }
}
