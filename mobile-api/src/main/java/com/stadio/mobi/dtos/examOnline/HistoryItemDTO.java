package com.stadio.mobi.dtos.examOnline;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class HistoryItemDTO {

    private String id;
    private String name;
    private Date time;
    private int submitCount;
    private double average;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private Date createdDate;

    private Map<String, Object> actions = new HashMap<>();

}
