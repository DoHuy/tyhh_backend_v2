package com.stadio.cms.dtos.theory;

import com.stadio.model.model.ExamIn;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TheoryFormDTO {

    private String id;

    @NotNull
    private String name;

    private String desc;

    @NotNull
    private String chapterId;

    @NotNull
    private String content;

    private List<ExamIn> examList;

}
