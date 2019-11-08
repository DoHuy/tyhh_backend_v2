package com.stadio.model.repository.main.custom;

import com.stadio.model.documents.ExamHot;

import java.util.List;

public interface ExamHotRepositoryCustom
{
    List<ExamHot> findAllExamHotWithPositionLessThan(Integer position);
}
