package com.stadio.model.dtos.cms;

import com.stadio.model.documents.Exam;
import lombok.Data;

import java.util.Date;

@Data
public class ExamDetailDTO extends ExamListDTO {

    private Date updated_date;
    private String created_by;
    private String updated_by;
    private String chapterId;

    public ExamDetailDTO(Exam exam)
    {
        super(exam);
        this.updated_date = exam.getUpdatedDate();
        this.created_by = exam.getCreatedBy();
        this.updated_by = exam.getUpdatedBy();
        this.chapterId = exam.getChapterId();
    }
}
