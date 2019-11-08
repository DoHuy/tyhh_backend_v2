package com.stadio.cms.controllers.content;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IChapterService;
import com.stadio.model.dtos.cms.ChapterFormDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "api/chapter", name = "Quản lý chương")
public class ChapterController extends BaseController {

    private Logger logger = LogManager.getLogger(ChapterController.class);

    @Autowired
    IChapterService chapterService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, name = "Tạo chương mới")
    public ResponseResult create(@RequestBody ChapterFormDTO chapterFormDTO) {
        return chapterService.processCreateOneChapter(chapterFormDTO);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, name = "Cập nhập chương")
    public ResponseResult update(@RequestBody ChapterFormDTO chapterFormDTO) {
        return chapterService.processUpdateOneChapter(chapterFormDTO);
    }

    @RequestMapping(value = "/by-id", method = RequestMethod.GET, name = "Lấy thông chương tin theo ID")
    public ResponseResult getById(@RequestParam(value = "id", required = true) String id) {
        return chapterService.processGetChapterById(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, name = "Lấy danh sách tất cả các chương")
    public ResponseResult getAll(HttpServletRequest request) {
        return chapterService.processGetAllChapter();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, name = "Xóa một chương đã tạo")
    public ResponseResult<?> delete(@RequestParam(value = "id", required = true) String id) {
        return chapterService.processDeleteChapter(id);
    }

}
