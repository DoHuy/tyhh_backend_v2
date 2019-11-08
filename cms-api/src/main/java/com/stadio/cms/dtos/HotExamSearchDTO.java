package com.stadio.cms.dtos;

import com.stadio.model.documents.Exam;
import lombok.Data;

@Data
public class HotExamSearchDTO
{
    private String id;
    private String name;
    private String code;

    public static HotExamSearchDTO with(Exam exam)
    {
        HotExamSearchDTO examSearchDTO = new HotExamSearchDTO();
        examSearchDTO.setId(exam.getId());
        examSearchDTO.setName(exam.getName());
        examSearchDTO.setCode(exam.getCode());

        return examSearchDTO;
    }
}
