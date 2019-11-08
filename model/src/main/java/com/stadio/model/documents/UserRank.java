package com.stadio.model.documents;

import lombok.Data;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Andy on 01/20/2018.
 */
@Data
@Document(collection = "tab_user_rank")
@CompoundIndexes({
        @CompoundIndex(name = "idx_rank_created_date", def = "{'created_date': -1}"),
})
public class UserRank implements Serializable
{
    @Id
    private String id;

    @Field(value = "user_id")
    private String userId;

    @Field(value = "rank_id")
    private String rankId;

    @Field(value = "point")
    private double point;

    @Field(value = "created_date")
    private Date createdDate;

    public UserRank(){
        this.point = 0;
        this.createdDate = new Date();
    }

}
