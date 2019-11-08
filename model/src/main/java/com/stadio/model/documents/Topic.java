package com.stadio.model.documents;

import com.stadio.model.model.BaseModel;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "tab_topic")
public class Topic extends BaseModel {

    private String id;

    @Field("class_obj")
    private String classObj;

    public Topic() {
        super();
    }
}
