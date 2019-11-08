package com.stadio.model.documents;

import com.stadio.model.enu.CommentType;
import com.stadio.model.enu.MessageLabel;
import com.stadio.model.model.BaseModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Document(collection = "tab_exam_comment")
@CompoundIndexes({
        @CompoundIndex(name = "idx_object_id_created_date", def = "{'object_id':1, 'created_date': -1}"),
})
@Data
public class Comment extends BaseModel {

    @Id
    private String id;

    @Field(value = "message")
    private String message;

    @Field(value = "user_id")
    private String userId;

    @Field(value = "object_id")
    private String objectId;

    @Field(value = "comment_type")
    private CommentType type;

    @Field(value = "message_label")
    private MessageLabel messageLabel;

    @Field(value = "send_time")
    private Date sendTime;

    @Field("topic_id")
    @TextIndexed
    private String topicId;

    @Field("has_replies")
    @Indexed
    private boolean hasReplies;

    @Field("parent_comment_id")
    @TextIndexed
    private String parentCommentId;

    public Comment() {
        super();
    }

    public void setMessageLabel(String messageLabel) {
        if (messageLabel == null || MessageLabel.valueOf(messageLabel) == null) {
            this.messageLabel = MessageLabel.PLAIN_TEXT;
        } else {
            this.messageLabel = MessageLabel.valueOf(messageLabel);
        }
    }
}
