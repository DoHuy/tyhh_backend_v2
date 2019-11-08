package com.stadio.model.documents;

import com.stadio.model.model.BaseModel;
import com.stadio.model.model.ExamIn;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "tab_theory")
public class Theory extends BaseModel {

    @Id
    private String id;

    private String name;

    private String desc;

    @Field("chapter_id")
    @TextIndexed
    private String chapterId;

    private String content;

    @Field("exam_list")
    private List<ExamIn> examList;

//    @Field(value = "created_date")
//    private Date createdDate;
//
//    @Field(value = "updated_date")
//    private Date updatedDate;

    public Theory() {
        super();
    }

}
