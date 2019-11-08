package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "tab_section")
public class Section {

    @Id
    private String id;

    @Field(value = "name")
    private String name;

    @Field(value = "position")
    private Long position;

    @Field(value = "deleted")
    private Boolean deleted;

    @Field(value = "course_id_ref")
    private String courseId;

    @Field(value = "created_date")
    private Date createdDate;

    public Section(){
        deleted = false;
        createdDate = new Date();
    }

}
