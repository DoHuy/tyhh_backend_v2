package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "tab_manager_role")
@Data
public class ManagerRole {

    @Id
    private String id;

    @Field(value = "manager_id")
    private String managerId;

    @Field(value = "role_id")
    private String roleId;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "updated_date")
    private Date updatedDate;

    public ManagerRole() {
        this.updatedDate = new Date();
        this.createdDate = new Date();
    }

    public ManagerRole(String managerId, String roleId) {
        this.updatedDate = new Date();
        this.createdDate = new Date();
        this.managerId = managerId;
        this.roleId = roleId;
    }

}
