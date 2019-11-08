package com.stadio.model.repository.main.impl;

import com.stadio.common.utils.HelperUtils;
import com.stadio.model.documents.Bookmark;
import com.stadio.model.enu.BookmarkType;
import com.stadio.model.repository.main.custom.BookmarkRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class BookmarkRepositoryImpl implements BookmarkRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Bookmark findOneByUserIdAndExamId(String userId, String examId) {

        Query query = (new Query()).addCriteria(
                Criteria.where("user_id").is(userId)
                        .and("object_id").is(examId)
                        .and("bookmark_type").is(BookmarkType.EXAM)
        );
        List<Bookmark> bookmarkList = mongoTemplate.find(query, Bookmark.class);

        if (HelperUtils.isEmptyArray(bookmarkList)) {
            //TODO Can delete code after migrate bookmark collection
            query = (new Query()).addCriteria(
                    Criteria.where("user_id").is(userId).and("exam_id").is(examId)
            );
            bookmarkList = mongoTemplate.find(query, Bookmark.class);
        }

        return (bookmarkList != null && bookmarkList.size() != 0) ? bookmarkList.get(0) : null;
    }

    @Override
    public Bookmark findOneByUserIdAndCategoryId(String userId, String categoryId) {
        Query query;
        query = (new Query()).addCriteria(
                Criteria.where("user_id").is(userId)
                        .and("object_id").is(categoryId)
                        .and("bookmark_type").is(BookmarkType.EXAM_CATEGORY)
        );
        List<Bookmark> bookmarkList = mongoTemplate.find(query, Bookmark.class);

        if (HelperUtils.isEmptyArray(bookmarkList)) {
            //TODO Can delete code after migrate bookmark collection
            query = (new Query()).addCriteria(
                    Criteria.where("user_id").is(userId).and("category_id").is(categoryId)
            );
            bookmarkList = mongoTemplate.find(query, Bookmark.class);
        }

        return (bookmarkList != null && bookmarkList.size() != 0) ? bookmarkList.get(0) : null;
    }

}
