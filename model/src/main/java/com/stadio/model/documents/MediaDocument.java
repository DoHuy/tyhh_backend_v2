package com.stadio.model.documents;

import com.stadio.model.model.BaseModel;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Data
@Document(collection = "tab_media_document")
public class MediaDocument extends BaseModel implements Serializable
{
    @Id
    private String id;

    @Field(value = "url")
    private String url;

    @Field(value = "uri")
    private String uri;

    @Field(value = "name")
    private String name;

    public MediaDocument() {
        super();
    }

}
