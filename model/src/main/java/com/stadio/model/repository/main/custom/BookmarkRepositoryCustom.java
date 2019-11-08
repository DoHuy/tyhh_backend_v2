package com.stadio.model.repository.main.custom;

import com.stadio.model.documents.Bookmark;
import com.stadio.model.enu.BookmarkType;

public interface BookmarkRepositoryCustom {

    Bookmark findOneByUserIdAndExamId(String userId, String ExamId);

    Bookmark findOneByUserIdAndCategoryId(String userId, String CategoryId);

}
