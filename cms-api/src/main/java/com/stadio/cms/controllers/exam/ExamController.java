package com.stadio.cms.controllers.exam;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.controllers.users.ManagerController;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IExamService;
import com.stadio.model.dtos.cms.ExamFormDTO;
import com.stadio.model.dtos.cms.ExamSearchFormDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Andy on 11/10/2017.
 */
@RestController
@RequestMapping(value = "api/exam", name = "Quản lý Đề thi")
public class ExamController extends BaseController {

    private Logger logger = LogManager.getLogger(ManagerController.class);

    @Autowired
    IExamService examService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, name = "Tạo đề thi")
    public ResponseResult create(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody ExamFormDTO examFormDTO) {
        return examService.processCreateOneExam(examFormDTO, token);
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, name = "Tìm kiếm đề thi")
    public ResponseResult search(HttpServletRequest request,
                                 @RequestHeader(value = "Authorization") String token,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                                 @RequestBody ExamSearchFormDTO examSearchFormDTO) {
        return examService.processSearchExam(token, examSearchFormDTO, page, pageSize, this.requestURI(request));
    }

    @RequestMapping(value = "/by-id", method = RequestMethod.GET, name = "Thông tin đề thi")
    public ResponseResult getExamById(HttpServletRequest request, @RequestParam(value = "id") String id) {
        return examService.processGetExamDetailForCMS(id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, name = "Cập nhật đề thi")
    public ResponseResult update(HttpServletRequest request,
                                 @RequestHeader(value = "Authorization") String token,
                                 @RequestBody ExamFormDTO examFormDTO) {
        return examService.processUpdateOneExam(examFormDTO, token);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, name = "Xóa đề thi")
    public ResponseResult delete(HttpServletRequest request,
                                 @RequestHeader(value = "Authorization") String token,
                                 @RequestParam(value = "id") String id) {
        return examService.processDeleteOneExam(id, token);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, name = "Xem danh sách đề thi")
    public ResponseResult list(HttpServletRequest request,
                               @RequestHeader(value = "Authorization") String token,
                               @RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize) {
        return examService.processGetListExam(token, page, pageSize, this.requestURI(request));
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET, name = "Xem chi tiết đề thi")
    public ResponseResult detail(@RequestParam(value = "id") String id) {
        return examService.processGetExamDetail(id);
    }

    @RequestMapping(value = "/question-nth", method = RequestMethod.GET, name = "Xem chi tiết câu hỏi")
    public ResponseResult getQuestionNthOfExam(HttpServletRequest request,
                                               @RequestParam(value = "id") String id,
                                               @RequestParam(value = "nth") Integer nth) {
        return examService.getQuestionNthOfExam(id, nth);
    }

    @RequestMapping(value = "/question-quantity", method = RequestMethod.GET)
    public ResponseResult getQuestionQuantityOfExam(@RequestParam(value = "id") String id) {
        return examService.getQuestionQuantityOfExam(id);
    }

    @RequestMapping(value = "/question-list", method = RequestMethod.GET, name = "Xem danh sách câu hỏi trong đề thi")
    public ResponseResult getQuestionOfExam(@RequestParam(value = "id") String id) {
        return examService.getQuestionOfExam(id);
    }
}
