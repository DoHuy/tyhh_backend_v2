package com.stadio.model.dtos.cms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Created by Andy on 11/13/2017.
 */
@Data
public class ExamFormDTO
{
    private String id;
    private String code;
    private String name;
    private Long time;
    private Integer price;
    private String chapterId;
    private Boolean enable;
    private String clazzId;
    private String type;
    private List<String> keywords;
    private String summary;
    private String imageUrl;
    private Integer questionMax;

    @JsonIgnoreProperties
    private Boolean deleted;
    private Boolean hasCorrectionDetail;

}
