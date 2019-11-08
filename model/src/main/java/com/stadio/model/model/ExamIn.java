package com.stadio.model.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ExamIn {

    @NotNull
    @Field("exam_id")
    private String examId;

}
