package com.stadio.model.dtos.cms;

import lombok.Data;

import java.util.List;

@Data
public class PracticeDTO
{
    private List<CategoryListDTO> hotCategories;

    private List<ExamListDTO> hotExam;

    private String hotExam_more_action;

    private List<ExamListDTO> newExam;

    private String newExam_more_action;
}
