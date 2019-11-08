package com.stadio.model.dtos.cms;

import com.stadio.model.documents.Category;
import lombok.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class CategoryListDTO
{
    private String id;
    private String name;
    private String summary;
    private Date createdDate;
    private Date updatedDate;
    private String createdBy;
    private String updatedBy;
    //private String details_action;

    private Map<String, Object> actions = new HashMap<>();

    public CategoryListDTO(Category category)
    {
        this.id = category.getId();
        this.name = category.getName();
        this.summary = category.getSummary();
        this.createdDate = category.getCreatedDate();
        this.updatedDate = category.getUpdatedDate();
        this.updatedBy = category.getUpdatedBy();
        this.createdBy = category.getCreatedBy();
    }
}
