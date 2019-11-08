package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "tab_user_achievement")
public class UserAchievement {
    @Id
    private String id;

    @Field(value = "user_id")
    private String userId;

    @Field(value = "achievement_id")
    private String achievementId;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "expired")
    private Date expired;

    public UserAchievement(){
        this.createdDate = new Date();
    }
}
