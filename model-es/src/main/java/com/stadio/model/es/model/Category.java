package com.stadio.model.es.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Category implements Serializable {

    private String id;
    private String name;
    private String summary;
    private int position;
    private String imageUrl;
    private String createdBy;
    private String updatedBy;
    private List<Exam> examIds;
    private Date createdDate;
    private Date updatedDate;

    public Category() {
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }
}
