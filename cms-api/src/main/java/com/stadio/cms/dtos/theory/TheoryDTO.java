package com.stadio.cms.dtos.theory;

import com.stadio.model.documents.Exam;
import com.stadio.model.documents.Theory;
import com.stadio.model.dtos.cms.ChapterFormDTO;
import com.stadio.model.model.ExamIn;
import com.stadio.model.repository.main.ChapterRepository;
import com.stadio.model.repository.main.ExamRepository;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TheoryDTO {

    private String id;

    private String name;

    private String desc;

    private String chapterId;

    private ChapterFormDTO chapter;

    private String content;

    private List<TheoryExamItemDTO> examList;

    public TheoryDTO(){}

    public TheoryDTO(Theory theory, ExamRepository examRepository, ChapterRepository chapterRepository) {
        this.id = theory.getId();
        this.name = theory.getName();
        this.desc = theory.getDesc();
        this.chapterId = theory.getChapterId();
        this.content = theory.getContent();
        this.examList = new ArrayList<>();
        if (theory.getExamList() != null) {
            for (ExamIn examInTheory: theory.getExamList()) {
                Exam exam = examRepository.findOne(examInTheory.getExamId());
                this.examList.add(new TheoryExamItemDTO(exam));
            }
        }
        this.chapter = new ChapterFormDTO(chapterRepository.findOne(theory.getChapterId()));
    }
}
