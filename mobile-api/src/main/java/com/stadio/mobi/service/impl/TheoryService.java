package com.stadio.mobi.service.impl;

import com.hoc68.users.documents.User;
import com.stadio.common.utils.ResponseCode;
import com.stadio.mobi.dtos.theory.TheoryDTO;
import com.stadio.mobi.dtos.theory.TheoryItemDTO;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.ITheoryService;
import com.stadio.model.documents.*;
import com.stadio.model.dtos.mobility.ExamItemDTO;
import com.stadio.model.model.ExamIn;
import com.stadio.model.repository.main.TheoryUserReadRepository;
import com.stadio.model.repository.main.ChapterRepository;
import com.stadio.model.repository.main.ExamRepository;
import com.stadio.model.repository.main.TheoryRepository;
import com.stadio.model.repository.main.UserExamRepository;
import com.stadio.model.repository.user.ManagerRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class TheoryService extends BaseService implements ITheoryService {

    @Autowired
    TheoryRepository theoryRepository;

    @Autowired
    ChapterRepository chapterRepository;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    UserExamRepository userExamRepository;

    @Autowired
    TheoryUserReadRepository theoryUserReadRepository;

    @Autowired
    ManagerRepository managerRepository;


    private Logger logger = LogManager.getLogger(TheoryService.class);

    @Override
    public ResponseResult findByChapterId(String id) {
        List<Theory> theories = theoryRepository.findAllByChapterId(id);
        List<TheoryItemDTO> theoryItemDTOList = new ArrayList<>();
        List<ExamItemDTO> theoryExamItemDTOList = new ArrayList<>();
        User current = this.getUserRequesting();

        if (theories != null) {
            for (Theory theory: theories) {
                TheoryItemDTO theoryDTO = TheoryItemDTO.with(theory);
                theoryItemDTOList.add(theoryDTO);

                //Set progress
                int countSubmitted = 0;
                List<ExamIn> examList = theory.getExamList();
                for (ExamIn examInTheory: examList) {
                    String examId = examInTheory.getExamId();
                    Exam exam = examRepository.findOne(examId);
                    ExamItemDTO theoryExamItemDTO = ExamItemDTO.with(exam);
                    if (current != null) {
                        UserExam userExam = userExamRepository.findByUserIdRefAndExamIdRef(current.getId(), examId);
                        if (userExam != null) {
                            countSubmitted++;
                            theoryExamItemDTO.setDidDone(true);
                        } else {
                            theoryExamItemDTO.setDidDone(false);
                        }
                    }
                    theoryExamItemDTOList.add(theoryExamItemDTO);
                }

                if (examList.size() == 0) {
                    theoryDTO.setExamProgress(0);
                } else {
                    theoryDTO.setExamProgress((double) countSubmitted/(double)examList.size());
                }

                if (theoryDTO.getExamProgress() > 0) {
                    theoryDTO.setIsRead(true);
                } else {
                    if (current != null){
                        theoryDTO.setIsRead((theoryUserReadRepository.countByUserIdRefAndTheoryIdRef(current.getId(), theory.getId())) != 0);
                    }
                }
            }
        }

        //Response
        HashMap<String, List> response = new HashMap<String, List>();
        response.put("exams", theoryExamItemDTOList);
        response.put("theories", theoryItemDTOList);

        return ResponseResult.newSuccessInstance(response);
    }

    @Override
    public ResponseResult markAsRead(String theoryId) {
        Theory theory = theoryRepository.findOne(theoryId);
        if (theory == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL,getMessage("theory.not.found"));
        }

        TheoryUserRead theoryUserRead = theoryUserReadRepository.findFirstByUserIdRefAndAndTheoryIdRef(this.getUserRequesting().getId(), theoryId);
        if (theoryUserRead != null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL,getMessage("theory.was.read"));
        }
        theoryUserRead = new TheoryUserRead();
        theoryUserRead.setTheoryIdRef(theoryId);
        theoryUserRead.setUserIdRef(this.getUserRequesting().getId());
        theoryUserReadRepository.save(theoryUserRead);

        return ResponseResult.newSuccessInstance(null);
    }

    @Override
    public ResponseResult getTheoryDetailsById(String theoryId) {
        Theory theory = theoryRepository.findOne(theoryId);

        if (theory != null) {
            TheoryDTO theoryDTO = new TheoryDTO(theory, examRepository, chapterRepository);
            int countSubmitted = 0;
            List<ExamItemDTO> examList = theoryDTO.getExamList();
            User current = this.getUserRequesting();
            if (current != null) {
                for (ExamItemDTO exam: examList) {
                    UserExam userExam = userExamRepository.findByUserIdRefAndExamIdRef(current.getId(),exam.getId());
                    if (userExam != null) {
                        exam.setDidDone(true);
                        countSubmitted++;
                    } else {
                        exam.setDidDone(false);
                    }
                }
            }

            if (examList.size() == 0) {
                theoryDTO.setExamProgress(0);
            } else {
                theoryDTO.setExamProgress((double) countSubmitted/(double)examList.size());
            }

            if (theoryDTO.getExamProgress() > 0) {
                theoryDTO.setIsRead(true);
            } else {
                if (current != null) {
                    theoryDTO.setIsRead((theoryUserReadRepository.countByUserIdRefAndTheoryIdRef(current.getId(), theory.getId())) != 0);
                }
            }

            try {
                SimpleDateFormat fm = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                if (theory.getCreatedDate() != null) {
                    theoryDTO.setCreatedDate(fm.format(theory.getCreatedDate()));
                } else {
                    theoryDTO.setCreatedDate(fm.format(new Date()));
                }
            } catch (Exception e) {
                logger.error("getTheoryDetailsById: ", e);
            }

            if (StringUtils.isNotBlank(theory.getCreatedBy())) {
                com.hoc68.users.documents.Manager manager = managerRepository.findOne(theory.getCreatedBy());
                theoryDTO.setCreatedBy(manager.getFullName());
            } else {
                theoryDTO.setCreatedBy("TYHH");
            }


            return ResponseResult.newSuccessInstance(theoryDTO);
        } else {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL,getMessage("theory.not.found"));
        }

    }
}
