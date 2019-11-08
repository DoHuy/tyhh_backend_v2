package com.stadio.model.documents;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stadio.model.enu.BookmarkType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by Andy on 01/20/2018.
 */
@Data
@Document(collection = "tab_bookmark")
public class Bookmark
{
    @JsonIgnore
    @Id
    private String id;

    @JsonIgnore
    @Field(value = "user_id")
    @TextIndexed
    private String userId;

    @Field(value = "exam_id")
    private String examId;

    @Field(value = "category_id")
    private String categoryId;

    @Field(value = "object_id")
    @TextIndexed
    private String objectId;

    @Field(value = "bookmark_type")
    @TextIndexed
    private BookmarkType bookmarkType;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "name")
    private String name;

    public Bookmark()
    {
        this.createdDate = new Date();
    }

}
