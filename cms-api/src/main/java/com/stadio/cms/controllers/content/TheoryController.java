package com.stadio.cms.controllers.content;

import com.stadio.cms.dtos.theory.TheoryFormDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.ITheoryService;
import com.stadio.common.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/theory", name = "Quản lý Lý thuyết")
public class TheoryController {

    @Autowired
    ITheoryService theoryService;

    @GetMapping(value = "", name = "Danh sách lý thuyết")
    public ResponseResult findTheoryByChapterId(@RequestParam("chapterId") String chapterId) {
        return theoryService.findByChapterId(chapterId);
    }

    @GetMapping(value = "/detail", name = "Xem chi tiết")
    public ResponseResult findTheoryByTheoryId(@RequestParam("theoryId") String theoryId) {
        return theoryService.findByTheoryId(theoryId);
    }

    @PostMapping(value = "/save", name = "Cập nhật lý thuyết")
    public ResponseResult save(@Valid @RequestBody TheoryFormDTO theoryFormDTO, BindingResult result) {
        if(result.hasErrors()) {
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,result.getAllErrors().toString());
        }
        return theoryService.saveTheory(theoryFormDTO);
    }

    @PostMapping(value = "/delete", name = "Xóa bài lý thuyết")
    public ResponseResult delete(@RequestParam("theoryId") String theoryId) {
        return theoryService.deleteTheory(theoryId);
    }

    @GetMapping(value = "/findExam", name = "Tìm kiếm bài tập")
    public ResponseResult findExamSumaryById(@RequestParam("examId") String examCode) {
        return theoryService.findExamSumaryByCode(examCode);
    }

}
