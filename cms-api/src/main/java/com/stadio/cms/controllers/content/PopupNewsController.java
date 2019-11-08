package com.stadio.cms.controllers.content;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.dtos.popupNews.PopupNewsFormDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IPopupNewsService;
import com.stadio.common.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/popup-news", name = "Quản lý popup")
public class PopupNewsController extends BaseController {

    @Autowired
    IPopupNewsService popupNewsService;

    @GetMapping(value = "/list", name = "Danh sách popup")
    public ResponseResult getListPopupNews() {
        return this.popupNewsService.getListPopupNews();
    }

    @PostMapping(value = "/add", name = "Thêm popup")
    public ResponseResult addNewPopupNews(@Valid @RequestBody PopupNewsFormDTO popupNewsFormDTO,
                                          BindingResult result) {
        if(result.hasErrors()) {
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,result.getAllErrors().toString());
        }
        return this.popupNewsService.addNewPopupNews(popupNewsFormDTO);
    }

    @PostMapping(value = "/hide", name = "Ẩn popup")
    public ResponseResult hideNewPopupNews(@RequestParam(value = "id", required = true) String id) {
        return this.popupNewsService.hidePopupNews(id);
    }

    @PostMapping(value = "/delete", name = "Xóa popup")
    public ResponseResult deletePopupNews(@RequestParam(value = "id", required = true) String id) {
        return this.popupNewsService.deletePopupNews(id);
    }

    @PostMapping(value = "/showInApp", name = "Hiển thị popup trong App")
    public ResponseResult makePopupNewsShowInApp(@RequestParam(value = "id", required = true) String id) {
        return this.popupNewsService.makePopupNewsShowInApp(id);
    }

}
