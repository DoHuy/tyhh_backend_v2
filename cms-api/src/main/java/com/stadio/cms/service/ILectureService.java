package com.stadio.cms.service;

import com.stadio.cms.dtos.course.QuestionInVideoFromDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.model.dtos.cms.LectureFormDTO;

import java.util.List;

public interface ILectureService {
    ResponseResult processGetListLectureSection(String sectionId);

    ResponseResult processDeleteLecture(String id);

    ResponseResult processGetDetailLecture(String lectureId);

    ResponseResult processAddExam(String lectureId,String examCode);

    ResponseResult processDeleteExam(String lectureId,String examCode);

    ResponseResult processGetVideoQuestion(String lectureId);

    ResponseResult processAddVideoQuestions(List<QuestionInVideoFromDTO> questionInVideoFromDTOList, String lectureId);

    ResponseResult processDeleteVideoQuestion(String lectureId, List<String> questionIds);

}
