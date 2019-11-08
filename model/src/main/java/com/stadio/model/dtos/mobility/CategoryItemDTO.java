package com.stadio.model.dtos.mobility;

import com.stadio.model.documents.Category;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class CategoryItemDTO
{
    private String id;
    private String name;
    private String summary;
    private Date createdDate;
    private Date updatedDate;
    private String createdBy;
    private String updatedBy;
    private long price;
    private long priceOld;

    private Map<String, Object> actions = new HashMap<>();

    public CategoryItemDTO(Category category)
    {
        this.id = category.getId();
        this.name = category.getName();
        this.summary = category.getSummary();
        this.createdDate = category.getCreatedDate();
        this.updatedDate = category.getUpdatedDate();
        this.updatedBy = category.getUpdatedBy();
        this.createdBy = category.getCreatedBy();
        this.price = category.getPrice();
        this.priceOld = category.getPriceOld();
    }

    public CategoryItemDTO(){

    }
}
