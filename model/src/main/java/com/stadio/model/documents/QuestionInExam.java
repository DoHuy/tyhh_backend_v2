package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "tab_question_in_exam", language = "vi")
public class QuestionInExam implements Serializable {
    @Id
    private String id;

    @Field(value = "position")
    Integer position;

    @DBRef()
    @Field(value = "question_id_ref")
    private Question question;

    @DBRef()
    @Field(value = "exam_id_ref")
    private Exam exam;
}
