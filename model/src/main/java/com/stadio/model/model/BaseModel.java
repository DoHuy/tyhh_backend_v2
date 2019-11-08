package com.stadio.model.model;

import java.util.Date;

import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
public class BaseModel {

	@Field(value = "created_date")
    protected Date createdDate;

    @Field(value = "updated_date")
    protected Date updatedDate;

    @Field(value = "created_by")
    @TextIndexed
    protected String createdBy;

    @Field(value = "updated_by")
    @TextIndexed
    protected String updatedBy;
    
    public BaseModel() {
    	this.createdDate = new Date();
        this.updatedDate = new Date();
	}
    
}
