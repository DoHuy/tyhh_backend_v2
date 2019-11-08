package com.stadio.mobi.dtos;

import com.stadio.model.dtos.mobility.CourseItemDTO;
import com.stadio.model.dtos.mobility.ExamItemDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClassItemDTO
{
    private String id;
    private String clazzName;
    private List<ChapterItemDTO> chapterList = new ArrayList<>();
    private List<ExamItemDTO> examList = new ArrayList<>();
    private List<CourseItemDTO> courseList = new ArrayList<>();
}
