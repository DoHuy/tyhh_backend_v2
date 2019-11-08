package com.stadio.mobi.service;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.model.dtos.mobility.CategoryItemDTO;

import java.util.List;

public interface ICategoryService
{

    List<CategoryItemDTO> getListCategory();

    ResponseResult processGetDetailCategory(String id, String token);

    ResponseResult processGetDetail(String id);

    ResponseResult processGetExamInCategory(String categoryId, int page, int pageSize);

    ResponseResult likeCategory(String categoryId);

    ResponseResult register(String categoryId);

    ResponseResult recommend(String categoryId, int page, int pageSize);

}
