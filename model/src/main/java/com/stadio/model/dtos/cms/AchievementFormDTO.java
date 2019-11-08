package com.stadio.model.dtos.cms;

import com.stadio.model.enu.ScheduleType;
import lombok.Data;

@Data
public class AchievementFormDTO {
    private String id;

    private String name;
    private String description;
    private String thumbnailUrl;
    private String condition;
    private ScheduleType scheduleType;
    private String collectionName;
}
