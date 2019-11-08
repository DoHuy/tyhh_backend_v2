package com.stadio.model.documents;

import com.stadio.model.enu.UserCourseAction;
import com.stadio.model.model.BaseModel;
import lombok.Data;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "tab_user_course")
public class UserCourse extends BaseModel {

    private String id;

    @Field("user_id_ref")
    @TextIndexed
    private String userIdRef;

    @Field("course_id_ref")
    @TextIndexed
    private String courseIdRef;

    @Field("action")
    private UserCourseAction action;

    @Field("extend_info")
    private Object extendInfo;

    public UserCourse() {
        super();
    }
}
