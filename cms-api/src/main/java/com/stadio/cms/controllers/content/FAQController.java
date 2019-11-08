package com.stadio.cms.controllers.content;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.dtos.faq.FAQFormDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IFAQService;
import com.stadio.model.documents.FAQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "api/faq", name = "Quản lý FAQ")
public class FAQController extends BaseController {

    @Autowired
    IFAQService ifaqService;

    @PostMapping(value = "/create", name = "Tạo câu hỏi")
    public ResponseEntity createdFAQ(
            @RequestBody @Valid FAQFormDTO faqFormDTO) {
        ResponseResult result = ifaqService.processCreateFAQ(faqFormDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/update", name = "Cập nhật câu hỏi")
    public ResponseEntity updateFAQ(
            @RequestBody @Valid FAQFormDTO faqFormDTO) {
        ResponseResult result = ifaqService.processUpdateFAQ(faqFormDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/delete", name = "Xóa câu hỏi")
    public ResponseEntity deleteFAQ(
            @RequestParam(value = "id") String id
    ) {
        ResponseResult result = ifaqService.processDeleteFAQ(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/list")
    public ResponseEntity list(@RequestParam(value = "groupId") String groupId) {
        List<FAQ> faqList = ifaqService.processGetListFAQInGroup(groupId);
        return ResponseEntity.ok(ResponseResult.newSuccessInstance(faqList));
    }

}
