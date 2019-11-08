package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by Andy on 03/02/2018.
 */
@Data
@Document(collection = "tab_fast_practice")
@CompoundIndexes({
        @CompoundIndex(name = "user_id_created_date", def = "{'user_id':1, 'created_date': -1}"),
})
public class FastPractice
{
    @Id
    private String id;

    @Field(value = "user_id")
    private String userId;

    @Field(value = "number_of_question")
    private int numberOfQuestion;

    @Field(value = "path_result")
    private String pathResult;

    @Field(value = "start_time")
    private Date startTime;

    @Field(value = "end_time")
    private Date endTime;

    @Field(value = "created_date")
    private Date createdDate;

    public FastPractice()
    {
        this.createdDate = new Date();
    }
}
