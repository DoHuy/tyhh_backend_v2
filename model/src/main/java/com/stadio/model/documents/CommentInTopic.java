package com.stadio.model.documents;


import lombok.Data;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tab_comment_in_topic")
@CompoundIndexes({
        @CompoundIndex(name = "idx_object_id_created_date", def = "{'object_id':1, 'created_date': -1}"),
})
@Data
public class CommentInTopic extends Comment {

    private long likeCount;

    public CommentInTopic() {
        super();
    }

    public void setLikeCount(long likeCount) {
        if (likeCount >= 0) {
            this.likeCount = likeCount;
        }
    }
}
