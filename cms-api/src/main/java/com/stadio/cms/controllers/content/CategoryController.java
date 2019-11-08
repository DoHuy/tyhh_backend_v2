package com.stadio.cms.controllers.content;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.face.Feature;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.ICategoryService;
import com.stadio.model.dtos.cms.CategoryFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "api/category", name = "Quản lý chuyên mục")
public class CategoryController extends BaseController
{
    @Autowired ICategoryService categoryService;

    @PostMapping(value = "/create", name = "Tạo chuyên mục")
    public ResponseResult create(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody CategoryFormDTO categoryFormDTO)
    {
        return categoryService.processCreateCategory(categoryFormDTO,token);
    }

    @PostMapping(value = "/update", name = "Cập nhật chuyên mục")
    public ResponseResult update(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody CategoryFormDTO categoryFormDTO)
    {
        return categoryService.processUpdateCategory(categoryFormDTO,token);
    }

    @GetMapping(value = "/delete", name = "Xóa chuyên mục")
    public ResponseResult delete(
            @RequestParam(value = "id") String id)
    {
        return categoryService.processDeleteCategory(id);
    }

    @PostMapping(value = "/addExam", name = "Thêm đề thi vào chuyên mục")
    public ResponseResult addExam(
            @RequestParam(value = "categoryId") String categoryId,
            @RequestParam(value = "examId", required = false) String examId,
            @RequestParam(value = "examCode", required = false) String examCode)

    {
        return categoryService.processAddExamToCategory(categoryId, examId, examCode);
    }

    @PostMapping(value = "/removeExam", name = "Xóa đề thi khỏi chuyên mục")
    public ResponseResult removeExam(
            @RequestParam(value = "categoryId") String categoryId,
            @RequestParam(value = "examId") String examId)
    {
        return categoryService.processRemoveExamFromCategory(categoryId, examId);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, name = "Danh sách các chuyên mục")
    public ResponseResult list()
    {
        return categoryService.processGetListCategory();
    }

    @GetMapping(value = "/details/{id:.+}", name = "Xem chi tiết một chuyên mục")
    public ResponseResult details(@PathVariable String id)
    {
        return categoryService.processGetDetailCategory(id);
    }

}
