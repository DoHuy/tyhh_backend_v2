package com.stadio.cms.service.impl;

import com.stadio.cms.dtos.course.QuestionInVideoFromDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.ILectureService;
import com.stadio.common.utils.HelperUtils;
import com.stadio.common.utils.ResponseCode;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Exam;
import com.stadio.model.documents.Lecture;
import com.stadio.model.documents.Question;
import com.stadio.model.dtos.cms.LectureDetailDTO;
import com.stadio.model.dtos.cms.LectureExamItemDTO;
import com.stadio.model.dtos.cms.LectureFormDTO;
import com.stadio.model.dtos.cms.LectureItemDTO;
import com.stadio.model.dtos.cms.course.QuestionInVideoDTO;
import com.stadio.model.model.ExamIn;
import com.stadio.model.model.course.QuestionInVideo;
import com.stadio.model.repository.main.ExamRepository;
import com.stadio.model.repository.main.LectureRepository;
import com.stadio.model.repository.main.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LectureService extends BaseService implements ILectureService {

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Override
    public ResponseResult processGetListLectureSection(String sectionId) {
        List<Lecture> lectureList = lectureRepository.findBySectionIdIsAndDeletedIsOrderByPositionAsc(sectionId,false);
        List<LectureItemDTO> lectureItemDTOList = new LinkedList<>();
        if(lectureList!=null){
            lectureList.stream().forEach(lecture -> {
                lectureItemDTOList.add(new LectureItemDTO(lecture));
            });
        }
        return ResponseResult.newSuccessInstance(lectureItemDTOList);
    }

    @Override
    public ResponseResult processDeleteLecture(String id) {
        Lecture lecture = lectureRepository.findOne(id);
        if(lecture!=null){
            lecture.setDeleted(true);
            lectureRepository.save(lecture);
        }
        return ResponseResult.newSuccessInstance("lecture.delete.success");
    }

    @Override
    public ResponseResult processGetDetailLecture(String lectureId) {
        Lecture lecture = lectureRepository.findOne(lectureId);
        if(lecture!=null){
            LectureDetailDTO lectureDetailDTO = new LectureDetailDTO(lecture);

            List<ExamIn> examInList = lecture.getExamList();
            examInList.stream().forEach(examIn -> {
                Exam exam = examRepository.findOne(examIn.getExamId());
                lectureDetailDTO.addExam(exam);
            });

            List<QuestionInVideoDTO> questionInVideoDTOS = new ArrayList<>();
            if (lecture.getQuestionInVideoList() != null) {
                for (QuestionInVideo questionInVideo: lecture.getQuestionInVideoList()) {
                    questionInVideoDTOS.add(new QuestionInVideoDTO(questionInVideo, questionRepository));
                }
            }
            lectureDetailDTO.addQuestions(questionInVideoDTOS);

            return ResponseResult.newSuccessInstance(lectureDetailDTO);
        }else
            return ResponseResult.newErrorInstance(ResponseCode.FAIL,"lecture.not.exist");
    }

    @Override
    public ResponseResult processAddExam(String lectureId,String examCode) {
        Lecture lecture = lectureRepository.findOne(lectureId);
        if(lecture!=null){
            Exam exam = examRepository.findExamByCode(examCode);
            if(exam!=null){
                ExamIn examIn = new ExamIn();
                examIn.setExamId(exam.getId());
                lecture.getExamList().add(examIn);
                lectureRepository.save(lecture);
                return ResponseResult.newSuccessInstance(examIn);
            }
        }
        return ResponseResult.newErrorInstance(ResponseCode.FILE_NOT_EXIST,"lecture.exam.not.exist");
    }

    @Override
    public ResponseResult processDeleteExam(String lectureId,String examCode) {
        Lecture lecture = lectureRepository.findOne(lectureId);
        if(lecture!=null){
            Exam exam = examRepository.findExamByCode(examCode);
            if(exam!=null){
                List<ExamIn> examInList =lecture.getExamList();
                for(int pos =0;pos<examInList.size();pos++){
                    ExamIn examIn = examInList.get(pos);
                    if(examIn.getExamId().equals(exam.getId())){
                        examInList.remove(examIn);
                        lecture.setExamList(examInList);
                        lectureRepository.save(lecture);
                        break;
                    }
                };
            }
        }
        return ResponseResult.newSuccessInstance("exam.lecture.delete.success");
    }

    @Override
    public ResponseResult processGetVideoQuestion(String lectureId) {
        Lecture lecture = lectureRepository.findOne(lectureId);
        if (lecture != null) {
            List<QuestionInVideoDTO> questionInVideoDTOS = new ArrayList<>();
            if (!HelperUtils.isEmptyArray(lecture.getQuestionInVideoList())) {
                for (QuestionInVideo questionInVideo: lecture.getQuestionInVideoList()) {
                    questionInVideoDTOS.add(new QuestionInVideoDTO(questionInVideo, questionRepository));
                }
            }
            Collections.sort(questionInVideoDTOS);
            return ResponseResult.newSuccessInstance(questionInVideoDTOS);
        } else {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL,"lecture.not.exist");
        }
    }

    @Override
    public ResponseResult processAddVideoQuestions(List<QuestionInVideoFromDTO> questionInVideoFromDTOList, String lectureId) {
        if (HelperUtils.isEmptyArray(questionInVideoFromDTOList)) {
            return ResponseResult.newErrorDefaultInstance(getMessage("lecture.question.in.video.can.not.empty"));
        }

        Lecture lecture = lectureRepository.findOne(lectureId);
        if (lecture == null) {
            return ResponseResult.newErrorDefaultInstance(getMessage("lecture.not.found"));
        }
        List<QuestionInVideo> questionInVideosCurrent = new ArrayList<>();
        if (!HelperUtils.isEmptyArray(lecture.getQuestionInVideoList())) {
            questionInVideosCurrent = new ArrayList<>(lecture.getQuestionInVideoList());
        }
        for (QuestionInVideoFromDTO question: questionInVideoFromDTOList) {
            if (question.getPositionInSecond() < 0 || question.getPositionInSecond() > lecture.getVideoDuration()) {
                //If position has different with video duration
                return ResponseResult.newErrorDefaultInstance(getMessage("lecture.question.in.video.wrong.position") + question.getQuestionId());
            }
            if (!HelperUtils.isEmptyArray(questionInVideosCurrent)) {
                //Check position/question exist
                for (QuestionInVideo q:questionInVideosCurrent) {
                    if (q.getPositionInSecond().equals(question.getPositionInSecond()) ||
                            q.getQuestionId().equals(question.getQuestionId())) {
                        return ResponseResult.newErrorDefaultInstance(getMessage("lecture.question.in.video.already.have.position") + question.getQuestionId());
                    }
                }
            }
            Question ques = questionRepository.findOne(question.getQuestionId());
            if (ques == null) {
                return ResponseResult.newErrorDefaultInstance(getMessage("lecture.question.in.video.question.id.not.found") + question.getQuestionId());
            }
            questionInVideosCurrent.add(new QuestionInVideo(question.getQuestionId(), question.getPositionInSecond()));
        }

        Collections.sort(questionInVideosCurrent);
        lecture.setQuestionInVideoList(questionInVideosCurrent);

        lectureRepository.save(lecture);

        return ResponseResult.newSuccessInstance(null);
    }

    @Override
    public ResponseResult processDeleteVideoQuestion(String lectureId, List<String> questionIds) {
        if (HelperUtils.isEmptyArray(questionIds)) {
            return ResponseResult.newErrorDefaultInstance(getMessage("lecture.question.in.video.can.not.empty"));
        }

        Lecture lecture = lectureRepository.findOne(lectureId);
        if (lecture == null) {
            return ResponseResult.newErrorDefaultInstance(getMessage("lecture.not.found"));
        }

        List<QuestionInVideo> questionInVideosCurrent = new ArrayList<>();
        if (!HelperUtils.isEmptyArray(lecture.getQuestionInVideoList())) {
            questionInVideosCurrent = new ArrayList<>(lecture.getQuestionInVideoList());
        } else {
            return ResponseResult.newSuccessInstance(null);
        }
        for (String questionId: questionIds) {
            //Check position/question exist
            for (QuestionInVideo q:lecture.getQuestionInVideoList()) {
                if (q.getQuestionId().equals(questionId)) {
                    questionInVideosCurrent.remove(q);
                }
            }
        }
        lecture.setQuestionInVideoList(questionInVideosCurrent);
        lectureRepository.save(lecture);
        return ResponseResult.newSuccessInstance(null);
    }
}
