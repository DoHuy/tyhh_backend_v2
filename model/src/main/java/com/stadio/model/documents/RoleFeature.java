package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "tab_role_feature")
@Data
public class RoleFeature {
    @Id
    private String id;

    @Field(value = "role_id")
    private String roleId;

    @Field(value = "feature_id")
    private String featureId;

    @Field(value = "priority")
    private int priority;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "updated_date")
    private Date updatedDate;

    public RoleFeature() {
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }

    public RoleFeature(String roleId, String featureId) {
        this.createdDate = new Date();
        this.updatedDate = new Date();
        this.roleId = roleId;
        this.featureId = featureId;
        this.priority = 0;
    }
}
