package com.stadio.cms.controllers.course;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.dtos.course.QuestionInVideoFromDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.ILectureService;
import com.stadio.model.dtos.cms.LectureFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/course", name = "Quản lý bài học")
public class LectureController extends BaseController {

    @Autowired
    ILectureService lectureService;

    //lecture-manager
    @RequestMapping(value = "/lecture-manager/lecture-of-section", method = RequestMethod.GET, name = "Danh sách Bài học")
    public ResponseResult getListLectureOfSection(@RequestParam(value = "id") String sectionId) {
        return lectureService.processGetListLectureSection(sectionId);
    }

    @RequestMapping(value = "/lecture-manager/by-id", method = RequestMethod.GET, name = "Thông tin từng Bài học")
    public ResponseResult GetDetailLecture(@RequestParam(value = "id") String lectureId) {
        return lectureService.processGetDetailLecture(lectureId);
    }

    @RequestMapping(value = "/lecture-manager/delete", method = RequestMethod.POST, name = "Xóa bài học")
    public ResponseResult deleteLecture(@RequestParam(value = "id") String id) {
        return lectureService.processDeleteLecture(id);
    }

    @RequestMapping(value = "/lecture-manager/add-exam", method = RequestMethod.POST, name = "Thêm bài tập")
    public ResponseResult addLectureExam(
            @RequestParam(value = "id") String lectureId,
            @RequestParam(value = "code") String examCode) {
        return lectureService.processAddExam(lectureId, examCode);
    }

    @RequestMapping(value = "/lecture-manager/remove-exam", method = RequestMethod.POST, name = "Xóa bài tập khỏi bài học")
    public ResponseResult deleteLectureExam(
            @RequestParam(value = "id") String lectureId,
            @RequestParam(value = "code") String examCode) {
        return lectureService.processDeleteExam(lectureId, examCode);
    }

    @GetMapping(value = "/lecture-manager/video-question")
    public ResponseResult getVideoQuestions(@RequestParam("lectureId") String lectureId) {
        return lectureService.processGetVideoQuestion(lectureId);
    }

    @PostMapping(value = "/lecture-manager/video-question/add")
    public ResponseResult addVideoQuestion(
            @RequestParam("lectureId") String lectureId,
            @RequestBody List<QuestionInVideoFromDTO> questionInVideoFromDTOList
    ) {
        return lectureService.processAddVideoQuestions(questionInVideoFromDTOList, lectureId);
    }

    @PostMapping(value = "/lecture-manager/video-question/delete")
    public ResponseResult deleteVideoQuestions(
            @RequestParam("lectureId") String lectureId,
            @RequestBody List<String> questionIds
    ) {
        return lectureService.processDeleteVideoQuestion(lectureId, questionIds);
    }
}
