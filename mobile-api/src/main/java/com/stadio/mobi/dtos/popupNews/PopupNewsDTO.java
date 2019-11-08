package com.stadio.mobi.dtos.popupNews;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import lombok.Data;

import java.util.Date;

@Data
public class PopupNewsDTO {

    private String id;

    private String title;

    private String desc;

    private String content;

    private String imageUrl;

    private String actionUrl;

    public PopupNewsDTO(PopupNews popupNews)
    {
        this.id = popupNews.getId();
        this.title = popupNews.getTitle();
        this.desc = popupNews.getDesc();
        this.content = popupNews.getContent();
        this.imageUrl = popupNews.getImageUrl();
        this.actionUrl = popupNews.getActionUrl();
    }
}

