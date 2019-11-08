package com.stadio.model.dtos.mobility;

import com.stadio.model.enu.PracticeType;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class PracticeListDTO
{
    private String id;
    private String name;
    private Date createdDate;
    private Date updatedDate;
    private String createdBy;
    private String updatedBy;
    private String thumbnailUrl;
    private PracticeType actionType;
    private Map<String, Object> actions = new HashMap<>();
}
