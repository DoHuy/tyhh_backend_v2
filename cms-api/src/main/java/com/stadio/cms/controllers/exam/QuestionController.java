package com.stadio.cms.controllers.exam;

import com.stadio.cms.controllers.BaseController;
import com.stadio.common.utils.ResponseCode;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IExamService;
import com.stadio.cms.service.IQuestionService;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Exam;
import com.stadio.model.dtos.cms.QuestionFormDTO;
import com.stadio.model.dtos.cms.QuestionSearchFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Andy on 11/10/2017.
 */
@RestController
@RequestMapping(value = "api/question", name = "Ngân hàng câu hỏi")
public class QuestionController extends BaseController {
    @Autowired
    IQuestionService questionService;

    @Autowired
    IExamService examService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, name = "Tạo câu hỏi")
    public ResponseResult<?> create(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(value = "id", required = false) String idExam,
            @RequestParam(value = "nth", required = false) Integer nth,
            @RequestBody QuestionFormDTO questionFormDTO) {
        //start process
        ResponseResult<?> results;
        if (StringUtils.isNotNull(idExam) && nth != null) {
            Exam exam = examService.findOne(idExam);
            if (exam != null) {
                results = questionService.processCreateOneQuestion(exam, nth, questionFormDTO, token);
            } else {
                results = ResponseResult.newInstance(ResponseCode.MISSING_PARAM, this.getMessage("exam.invalid.exam"), null);
            }

        } else {
            results = questionService.processCreateOneQuestion(questionFormDTO, token);
        }
        //end process
        return results;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, name = "Cập nhật câu hỏi")
    public ResponseResult create(
            @RequestParam(value = "id", required = false) String idExam,
            @RequestParam(value = "nth", required = false) Integer nth,
            @RequestHeader(value = "Authorization") String token,
            @RequestBody QuestionFormDTO questionFormDTO) {
        //start process
        ResponseResult<?> results;
        if (StringUtils.isNotNull(idExam) && nth != null) {
            Exam exam = examService.findOne(idExam);
            if (exam != null) {
                results = questionService.processUpdateOneQuestion(exam, nth, questionFormDTO, token);
            } else {
                results = ResponseResult.newInstance(ResponseCode.MISSING_PARAM, this.getMessage("exam.invalid.exam"), null);
            }

        } else {
            results = questionService.processUpdateOneQuestion(questionFormDTO, token);
        }
        //end process
        return results;
    }


    @RequestMapping(value = "/search", method = RequestMethod.POST, name = "Tìm kiếm câu hỏi")
    public ResponseResult search(HttpServletRequest request,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                                 @RequestBody QuestionSearchFormDTO questionSearchFormDTO) {
        return questionService.processSearchQuestion(questionSearchFormDTO, page, pageSize, this.requestURI(request));
    }

    @RequestMapping(value = "/by-id", method = RequestMethod.GET, name = "Xem thông tin câu hỏi")
    public ResponseResult getQuestionId(@RequestParam(value = "id", required = true) String id) {
        return questionService.processGetQuestionById(id);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, name = "Xóa câu hỏi")
    public ResponseResult delete(@RequestParam(value = "id") String id) {
        return questionService.processDeleteOneQuestion(id);
    }

}
