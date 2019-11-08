package com.stadio.model.documents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.stadio.model.enu.QuestionLevel;
import com.stadio.model.enu.QuestionType;
import com.stadio.model.model.Answer;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by Andy on 11/10/2017.
 */
@Data
@Document(collection = "tab_question", language = "vi")
public class Question implements Serializable
{
    @Id
    private String id;

    @Field(value = "content")
    private String content;

    @Field(value = "answers")
    private List<Answer> answers = new ArrayList<>();

    @Field(value = "type")
    private QuestionType type;

    @Field(value = "level")
    private QuestionLevel level;

    @Field(value = "clazz_id_ref")
    private String clazzId;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "updated_date")
    private Date updatedDate;

    @Field(value = "created_by")
    private String createdBy;

    @Field(value = "updated_by")
    private String updatedBy;

    @Field( value = "explain")
    private String explain;

    @Field(value = "chapter_id_ref")
    private String chapterId;

//    @DBRef(lazy = true)
//    @Field(value = "exam_ids")
//    private List<Exam> examIds;

    public Question()
    {
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }

    @Override
    public String toString()
    {
        return "Question{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", answers=" + answers +
                ", type=" + type +
                ", level=" + level +
                ", clazz=" + clazzId +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", explain='" + explain + '\'' +
                ", chapterId='" + chapterId + '\'' +
                '}';
    }

    @JsonIgnoreProperties
    public String getCorrectAnswer()
    {
        for (Answer answer: this.answers)
        {
            if (answer.isCorrect()) return answer.getCode();
        }
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id);
    }
}
