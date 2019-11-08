package com.stadio.mobi.dtos.theory;


import com.stadio.model.documents.Exam;
import com.stadio.model.documents.Theory;
import com.stadio.model.dtos.cms.ChapterFormDTO;
import com.stadio.model.dtos.mobility.ExamItemDTO;
import com.stadio.model.model.ExamIn;
import com.stadio.model.repository.main.ChapterRepository;
import com.stadio.model.repository.main.ExamRepository;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private Boolean isRead;

    private double examProgress;

    private String createdBy;

    private String createdDate;

    private List<ExamItemDTO> examList;

    @Getter(AccessLevel.NONE)
    private Logger logger = LogManager.getLogger(TheoryDTO.class);

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
                try {
                    Exam exam = examRepository.findOne(examInTheory.getExamId());
                    this.examList.add(ExamItemDTO.with(exam));
                } catch (Exception e) {
                    logger.error("Get Exam error", e);
                }
            }
        }
        this.chapter = new ChapterFormDTO(chapterRepository.findOne(theory.getChapterId()));
    }
}
