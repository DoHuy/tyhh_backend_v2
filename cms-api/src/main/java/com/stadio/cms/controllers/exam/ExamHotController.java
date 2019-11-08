package com.stadio.cms.controllers.exam;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IExamService;
import com.stadio.cms.service.IHotExamService;
import com.stadio.model.dtos.cms.ExamHotFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "api/examHot", name = "Quản lý đề thi nổi bật")
public class ExamHotController extends BaseController {
    @Autowired
    IHotExamService hotExamService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, name = "Thêm đề thi nổi bật")
    public ResponseResult create(@RequestBody ExamHotFormDTO examHotFormDTO) {
        return hotExamService.processCreateExamHot(examHotFormDTO);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, name = "Cập nhật đề thi nổi bật")
    public ResponseResult update(@RequestBody ExamHotFormDTO examHotFormDTO) {
        return hotExamService.processUpdateExamHot(examHotFormDTO);
    }

    @GetMapping(value = "/delete", name = "Xóa đề thi nổi bật")
    public ResponseResult update(@RequestParam(value = "id") String id) {
        return hotExamService.processDeleteExamHot(id);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, name = "Danh sách đề thi nổi bật")
    public ResponseResult list(
            @RequestParam(value = "topType", required = false, defaultValue = "") String topType) {
        return hotExamService.processGetListExamHot(topType);
    }

    @GetMapping(value = "/search", name = "Tìm kiếm đề thi")
    public ResponseEntity searchExam(@RequestParam(value = "q", required = false, defaultValue = "") String q) {
        ResponseResult result = hotExamService.processSearchExam(q);
        return ResponseEntity.ok(result);
    }
}

