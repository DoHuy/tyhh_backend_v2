package com.stadio.model.repository.main;

import com.stadio.model.documents.Bookmark;
import com.stadio.model.enu.BookmarkType;
import com.stadio.model.repository.main.custom.BookmarkRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Andy on 01/20/2018.
 */
public interface BookmarkRepository extends MongoRepository<Bookmark, String>, BookmarkRepositoryCustom
{
    List<Bookmark> findBookmarksByUserIdOrderByCreatedDateDesc(String userId, Pageable pageable);

    List<Bookmark> findAllByBookmarkTypeIsNull();

    Bookmark findOneByUserIdAndObjectIdAndBookmarkType(String userId, String objectId, BookmarkType bookmarkType);

    long countByObjectIdAndBookmarkType(String objectId, BookmarkType bookmarkType);

}
