package com.stadio.cms.controllers.content;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.dtos.faq.FAQGroupFormDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IFAQGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/faqGroup", name = "Quản lý nhóm FAQ")
public class FAQGroupController extends BaseController {
    @Autowired
    IFAQGroupService ifaqGroupService;

    @GetMapping(value = "/list", name = "Danh sách nhóm FAQ")
    public ResponseEntity getList(
    ) {
        ResponseResult result = ifaqGroupService.processGetListFAQGroup();
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/create", name = "Tạo nhóm FAQ")
    public ResponseEntity createdFAQGroup(
            @RequestBody @Valid FAQGroupFormDTO faqGroupFormDTO) {
        ResponseResult result = ifaqGroupService.processCreateFAQGroup(faqGroupFormDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/update", name = "Cập nhật thông tin nhóm FAQ")
    public ResponseEntity updateFAQGroup(
            @RequestBody @Valid FAQGroupFormDTO faqGroupFormDTO) {
        ResponseResult result = ifaqGroupService.processUpdateFAQGroup(faqGroupFormDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/delete", name = "Xóa nhóm FAQ")
    public ResponseEntity deleteFAQGroup(
            @RequestParam(value = "id") String id
    ) {
        ResponseResult result = ifaqGroupService.processDeleteFAQGroup(id);
        return ResponseEntity.ok(result);
    }

}
