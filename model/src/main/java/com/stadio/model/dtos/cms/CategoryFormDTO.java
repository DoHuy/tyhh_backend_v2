package com.stadio.model.dtos.cms;

import lombok.Data;

@Data
public class CategoryFormDTO
{
    private String id;
    private String name;
    private String summary;
    private String imageUrl;
    private int position;
    private long price;
    private long priceOld;
    private Boolean hasCorrectionDetail;
    private String teacherId;
}
