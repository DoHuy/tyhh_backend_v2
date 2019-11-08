package com.stadio.model.documents;

import com.stadio.model.enu.UserPointType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "tab_user_point")
@CompoundIndexes({
        @CompoundIndex(name = "idx_created_date_user_id", def = "{'created_date': -1, 'user_id':1}"),
})
public class UserPoint {

    @Id
    private String id;

    @Field(value = "user_id")
    private String userId;

    @Field(value = "point")
    private Double point;

    @Field(value = "source_name")
    private String sourceName;

    @Field(value = "source_type")
    private UserPointType sourceType;

    @Field(value = "source_id")
    private String sourceId;

    @Field(value = "created_date")
    private Date createdDate;

    public UserPoint(){
        this.createdDate = new Date();
    }
}
