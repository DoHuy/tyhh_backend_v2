package com.stadio.mobi.controllers;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IFastPracticeService;
import com.stadio.model.model.UserAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Andy on 01/14/2018.
 */
@RestController
@RequestMapping(value = "api/practice")
public class PracticeController extends BaseController
{
    @Autowired IFastPracticeService practiceService;

    @GetMapping(value = "/fast/normal")
    public ResponseEntity fastPracticeNormal(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(value = "sessionId", required = false, defaultValue = "") String sessionId)
    {
        ResponseResult result = practiceService.processGetRandomQuestion(token, sessionId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/fast/payment")
    public ResponseEntity fastPracticePayment(
            @RequestHeader(value = "Authorization", required = false) String token)
    {
        ResponseResult result = practiceService.processGetRandomQuestionWithPayment(token);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/fast/submit/{sessionId}")
    public ResponseEntity actionSubmitFastPractice(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable String sessionId,
            @RequestBody List<UserAnswer> answers)
    {
        ResponseResult result = practiceService.processSubmitQuestion(token, sessionId, answers);
        return ResponseEntity.ok(result);
    }

}
