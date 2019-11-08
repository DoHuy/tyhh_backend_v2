package com.stadio.model.dtos.mobility;

import com.stadio.model.documents.Bookmark;
import com.stadio.model.enu.BookmarkType;
import lombok.Data;

import java.util.Date;

@Data
public class BookmarkDetailDTO {

    private String id;

    private String userId;

    private String objectId;

    private BookmarkType bookmarkType;

    private Date createdDate;

    private String name;

    private Object bookmarkDetail;

    public BookmarkDetailDTO(Bookmark bookmark) {
        this.id = bookmark.getId();
        this.userId = bookmark.getUserId();
        this.objectId = bookmark.getObjectId();
        this.bookmarkType = (bookmark.getBookmarkType());
        this.createdDate = bookmark.getCreatedDate();
        this.name = bookmark.getName();
    }
}
