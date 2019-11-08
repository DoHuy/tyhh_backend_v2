package com.stadio.model.documents;

import com.stadio.model.enu.AchievementType;
import com.stadio.model.enu.ScheduleType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Andy on 01/20/2018.
 */
@Data
@Document(collection = "tab_achievements")
public class Achievement implements Serializable
{
    @Id
    private String id;

    @Field(value = "name")
    private String name;

    @Field(value = "thumbnail_url")
    private String thumbnailUrl;

    @Field(value = "description")
    private String description;

    @Field(value = "condition")
    private String condition;

    @Field(value = "collection_name")
    private String collectionName;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "updated_date")
    private Date updatedDate;

    @Field(value = "deleted")
    private boolean deleted;

    @Field(value = "achievement_type")
    private AchievementType achievementType;

    @Field(value = "schedule_type")
    private ScheduleType scheduleType;

    public Achievement()
    {
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }
}
