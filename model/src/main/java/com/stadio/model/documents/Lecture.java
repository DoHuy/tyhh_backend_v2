package com.stadio.model.documents;

import com.stadio.model.model.BaseModel;
import com.stadio.model.model.ExamIn;
import com.stadio.model.model.course.QuestionInVideo;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "tab_lecture")
public class Lecture extends BaseModel{
    @Id
    private String id;

    @Field(value = "name")
    private String name;

    @Field(value = "deleted")
    private Boolean deleted;

    @Field(value = "section_id_ref")
    private String sectionId;

    @Field(value = "position")
    private Long position;

    @Field("exam_list")
    private List<ExamIn> examList;

    @Field("question_in_video_list")
    private List<QuestionInVideo> questionInVideoList;

    @Field(value = "video_url")
    private String videoUrl;

    @Field(value = "video_duration")
    private int videoDuration;

    @Field(value = "content")
    private String content;

    @Field(value = "is_free")
    private Boolean isFree;

    @Field(value = "document_url")
    private String documentUrl;

    @Field(value = "release_date")
    private Date releaseDate;

    @Field(value = "topic_id")
    private String topicId;

    public Lecture(){
        deleted = false;
        isFree = Boolean.FALSE;
        examList = new ArrayList<>();
    }
}
