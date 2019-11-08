package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "tab_faq_group")
public class FAQGroup {

    @Id
    private String id;

    @Field(value = "name")
    private String name;

    @Field(value = "description")
    private String description;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "updated_date")
    private Date updatedDate;

    @Field(value = "created_by")
    private String createdBy;

    @Field(value = "updated_by")
    private String updatedBy;

    @Field(value = "deleted")
    private  Boolean deleted;

    public FAQGroup() {
        this.createdDate = new Date();
        this.updatedDate = new Date();
        this.deleted = false;
    }
}
