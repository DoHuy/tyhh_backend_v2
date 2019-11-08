package com.stadio.cms.service.impl;

import com.stadio.cms.dtos.theory.TheoryDTO;
import com.stadio.cms.dtos.theory.TheoryExamItemDTO;
import com.stadio.cms.dtos.theory.TheoryFormDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.ITheoryService;
import com.stadio.common.utils.ResponseCode;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Exam;
import com.stadio.model.documents.Theory;
import com.stadio.model.repository.main.ChapterRepository;
import com.stadio.model.repository.main.ExamRepository;
import com.stadio.model.repository.main.TheoryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TheoryService extends BaseService implements ITheoryService {

    @Autowired
    TheoryRepository theoryRepository;

    @Autowired
    ChapterRepository chapterRepository;

    @Autowired
    ExamRepository examRepository;

    private Logger logger = LogManager.getLogger(TheoryService.class);

    @Override
    public ResponseResult findByChapterId(String id) {
        List<Theory> theories = theoryRepository.findAllByChapterId(id);

        List<TheoryDTO> theoryDTOList = new ArrayList<>();

        if (theories != null) {
            for (Theory theory: theories) {
                TheoryDTO theoryDTO = new TheoryDTO(theory, examRepository, chapterRepository);
                theoryDTOList.add(theoryDTO);
            }
        }

        return ResponseResult.newSuccessInstance(theoryDTOList);
    }

    @Override
    public ResponseResult findByTheoryId(String id) {
        Theory theory = theoryRepository.findOne(id);
        if (theory == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("theory.not.found"));
        }
        return ResponseResult.newSuccessInstance(new TheoryDTO(theory, examRepository, chapterRepository));
    }

    @Override
    public ResponseResult saveTheory(TheoryFormDTO theoryFormDTO) {
        Theory theory;
        if (StringUtils.isNotNull(theoryFormDTO.getId())) {
            theory = theoryRepository.findOne(theoryFormDTO.getId());
            if (theory == null) {
                return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("theory.not.found"));
            }
        } else {
            theory = new Theory();
        }

        if (chapterRepository.findOne(theoryFormDTO.getChapterId()) == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("chapter.invalid.id"));
        }

        theory.setChapterId(theoryFormDTO.getChapterId());
        theory.setContent(theoryFormDTO.getContent());
        theory.setExamList(theoryFormDTO.getExamList());
        theory.setName(theoryFormDTO.getName());
        theory.setDesc(theoryFormDTO.getDesc());
        theory.setUpdatedDate(new Date());
        
        if (StringUtils.isNotNull(theoryFormDTO.getDesc())) {
            theory.setDesc(theory.getDesc());
        }

        try {
            theoryRepository.save(theory);
            return ResponseResult.newSuccessInstance(new TheoryDTO(theory, examRepository, chapterRepository));
        } catch (Exception e) {
            logger.error("Save Theory error", e);
            return null;
        }
    }

    @Override
    public ResponseResult deleteTheory(String theoryId) {
        Theory theory = theoryRepository.findOne(theoryId);
        if (theory == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("theory.not.found"));
        }
        theoryRepository.delete(theoryId);
        return ResponseResult.newSuccessInstance(null);
    }

    @Override
    public ResponseResult findExamSumaryByCode(String examCode) {
        Exam exam = examRepository.findExamByCode(examCode);
        if (exam == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("exam.not.exist"));
        }
        return ResponseResult.newSuccessInstance(new TheoryExamItemDTO(exam));
    }
}
