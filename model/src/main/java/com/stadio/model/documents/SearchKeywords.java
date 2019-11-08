package com.stadio.model.documents;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Andy on 01/09/2018.
 */
@Data
@Document(collection = "tab_search_keywords")
public class SearchKeywords implements Serializable
{
    @Id
    private String id;

    @Field(value = "keyword")
    @Indexed(name = "idx_keyword")
    private String keyword;

    @Field(value = "total_search")
    private Integer totalSearch;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Field(value = "created_date")
    private Date createdDate;

    public SearchKeywords() {
        this.createdDate = new Date();
    }

    public static SearchKeywords newInstance() {
        return new SearchKeywords();
    }

}
