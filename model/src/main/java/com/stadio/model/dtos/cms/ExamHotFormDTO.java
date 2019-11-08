package com.stadio.model.dtos.cms;

import lombok.Data;

@Data
public class ExamHotFormDTO
{
    private String id;
    private String examIdRef;
    private String examCode;
    private Integer position;
    private String topType;
}
