package com.stadio.mobi.controllers;

import com.stadio.mobi.dtos.ExamSubmitDTO;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.ICategoryService;
import com.stadio.mobi.service.IExamService;
import com.stadio.mobi.service.IPracticeService;
import com.stadio.mobi.service.IQuestionService;
import com.stadio.model.model.UserAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Andy on 01/13/2018.
 */
@RestController
@RequestMapping(value = "api/exam")
public class ExamController extends BaseController
{
    @Autowired ICategoryService categoryService;

    @Autowired IExamService examService;

    @Autowired IQuestionService questionService;

    @Autowired IPracticeService practiceService;

    @GetMapping(value = "/categories")
    public ResponseEntity getListCategories()
    {
        ResponseResult result = ResponseResult.newSuccessInstance(categoryService.getListCategory());
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/newest")
    public ResponseEntity getNewestExam(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "limit", defaultValue = "100", required = false) int limit
            )
    {
        ResponseResult result = examService.processGetNewestExam(token, page, limit);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/details")
    public ResponseEntity getExamDetails(
            @RequestParam(value = "id") String id)
    {
        ResponseResult result = examService.processGetExamDetailForMobile(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/questionByExam")
    public ResponseEntity getQuestionByExam(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(value = "examId") String examId)
    {
        ResponseResult result = questionService.processGetQuestionForPractice(token, examId);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/submit/{examId}")
    public ResponseEntity actionSubmit(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable(value = "examId") String examId,
            @RequestBody ExamSubmitDTO examSubmitDTO)
    {
        ResponseResult result = practiceService.processSubmitResults(token, examSubmitDTO, examId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/answers")
    public ResponseEntity getAnswers(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(value = "examId") String examId)
    {
        ResponseResult result = practiceService.processGetAnswers(token, examId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/like")
    public ResponseEntity actionLike(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(value = "examId") String examId)
    {
        ResponseResult result = examService.processUserActionLike(token, examId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/topSubmit")
    public ResponseEntity getExamTopSubmit(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "limit", defaultValue = "100", required = false) int limit
    ) {
        ResponseResult result = examService.processGetExamTopSubmit(token, page, limit);
        return ResponseEntity.ok(result);
    }
}
