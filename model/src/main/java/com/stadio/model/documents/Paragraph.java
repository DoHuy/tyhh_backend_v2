package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "tab_paragraph")
public class Paragraph {
    @Id
    private String id;

    @Field(value = "name")
    private String name;

    @Field(value = "video_url")
    private String videoUrl;

    @Field(value = "video_duration")
    private int videoDuration;

    @Field(value = "content")
    private String content;

    @Field(value = "lecture_id_ref")
    private String lectureId;

    @Field(value = "is_free")
    private Boolean isFree;

    @Field(value = "document_url")
    private String documentUrl;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "deleted")
    private Boolean deleted;

    public Paragraph(){
        createdDate = new Date();
        deleted = false;
        isFree = Boolean.FALSE;
    }
}
