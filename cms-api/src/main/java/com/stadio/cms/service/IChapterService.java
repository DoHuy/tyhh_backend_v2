package com.stadio.cms.service;

import com.stadio.cms.response.ResponseResult;
import com.stadio.model.dtos.cms.ChapterFormDTO;

public interface IChapterService {

    ResponseResult processCreateOneChapter(ChapterFormDTO chapterFormDTO);

    ResponseResult processUpdateOneChapter(ChapterFormDTO chapterFormDTO);

    ResponseResult processGetChapterById(String id);

    ResponseResult processGetAllChapter();

    ResponseResult processDeleteChapter(String id);
}
