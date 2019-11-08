package com.stadio.model.dtos.cms;

import lombok.Data;

import java.util.Date;

/**
 * Created by sm on 12/8/17.
 */
@Data
public class ExamSearchFormDTO {
    private String code;
    private String name;
    private String clazzId;
    private String type;
    private Boolean hasCorrectionDetail;
    private Date fromDate;
    private Date toDate;
}
