package com.stadio.model.documents;

import com.stadio.model.model.CorrectAnswerInChapter;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Document(collection = "tab_user_exam_statistic")
@Data
public class UserExamStatistics {
    @Id
    private String id;

    @Field(value = "exam_id_ref")
    private String examIdRef;

    @Field(value = "user_id")
    private String userIdRef;

    @Field(value = "type1")
    private String type1;

    @Field(value = "type2")
    private Map<Integer,Integer> type2;

    @Field(value = "type3")
    private Map<String,CorrectAnswerInChapter> type3;

}
