package com.stadio.model.documents;

import com.stadio.model.model.BaseModel;
import lombok.Data;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "tab_exam_category")
public class ExamCategory extends BaseModel {

    private String id;

    @Field("exam_id")
    @TextIndexed
    private String examId;

    @Field("category_id")
    @TextIndexed
    private String categoryId;

    public ExamCategory() {
        super();
    }
}
