package com.stadio.task.controllers;

import com.stadio.task.service.InsertionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Andy on 01/20/2018.
 */
@RestController
@RequestMapping(value = "insertion")
public class InsertionController
{
    @Autowired InsertionService insertionService;

    @GetMapping("gender")
    public String gender()
    throws Exception
    {
        insertionService.processGeneratingNames();
        return "SUCCESS";
    }

    @GetMapping("question")
    @Async
    public String question()
    throws Exception
    {
        insertionService.processInsert2QuestionBank();
        return "SUCCESS";
    }

    @GetMapping("exam")
    @Async
    public String exam()
            throws Exception
    {
        insertionService.processInsert2Exam();
        return "SUCCESS";
    }

    /**
     *
     *
     * @return
     * @throws Exception
     */
    @GetMapping("user-exam-submit")
    @Async
    public String userExamSubmit()
            throws Exception
    {
        insertionService.processInsert2UserExamSubmit();
        return "SUCCESS";
    }
}
