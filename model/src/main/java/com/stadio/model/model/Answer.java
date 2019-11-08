package com.stadio.model.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by Andy on 11/10/2017.
 */
@Data
public class Answer
{
    @Field(value = "code")
    private String code;

    @Field(value = "content")
    private String content;

    @Field(value = "is_correct")
    private boolean isCorrect;
}
