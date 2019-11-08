package com.stadio.cms.dtos.examOnline;

import lombok.Data;

@Data
public class TablePointDTO {

    private String id;
    private String username;
    private String fullname;
    private String avatar;
    private int position;
    private long duration;
    private double point;

}