package com.stadio.model.documents;

import com.stadio.model.enu.RankType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Andy on 01/20/2018.
 */
@Data
@Document(collection = "tab_rank")
public class Rank
{
    @Id
    private String id;

    @Field(value = "start_time")
    private Date startTime;

    @Field(value = "end_time")
    private Date endTime;

    @Field(value = "rank_type")
    private RankType rankType;

    @Field(value = "name")
    private String name;

    @Field(value = "created_date")
    private Date createdDate;

}
