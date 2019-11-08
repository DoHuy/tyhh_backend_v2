package com.stadio.model.es.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Andy on 11/10/2017.
 */
@Data
public class Exam implements Serializable
{
    @Id
    private String id;
    private String code;
    private String name;
    private List<String> keywords;
    private String summary;
    private long time; //second
    private int price;
    private boolean enable;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date updatedDate;
    private String createdBy;
    private String updatedBy;
    private String clazzId;
    private String type;
    private boolean deleted;
    private Long views;
    private String chapterId;
    private String imageUrl;
    private int likes;
    private int submitCount;
    private double average;
    private int questionQuantity;
    private int questionMax;

}
