package com.stadio.cms.service;

import com.stadio.cms.response.ResponseResult;
import com.stadio.model.dtos.cms.CategoryFormDTO;

public interface ICategoryService
{
    ResponseResult processCreateCategory(CategoryFormDTO categoryFormDTO, String token);

    ResponseResult processUpdateCategory(CategoryFormDTO categoryFormDTO, String token);

    ResponseResult processDeleteCategory(String id);

    ResponseResult processAddExamToCategory(String categoryId, String examID, String examCode);

    ResponseResult processGetListCategory();

    ResponseResult processGetDetailCategory(String id);

    ResponseResult processRemoveExamFromCategory(String categoryId, String examId);

}
