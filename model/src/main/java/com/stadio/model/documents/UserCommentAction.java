package com.stadio.model.documents;

import com.stadio.model.enu.UserCommentActionType;
import com.stadio.model.model.BaseModel;
import lombok.Data;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "tab_user_comment_action")
public class UserCommentAction extends BaseModel{

    private String id;

    @Field("user_id_ref")
    @TextIndexed
    private String userIdRef;

    @Field("comment_id_ref")
    @TextIndexed
    private String commentIdRef;

    @Field("action")
    @TextIndexed
    private UserCommentActionType action;

    @Field("extend_info")
    private Object extendInfo;

    public UserCommentAction() {
        super();
    }

}
