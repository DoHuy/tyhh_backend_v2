package com.stadio.cms.dtos.popupNews;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.model.documents.PopupNews;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.TimeZone;

@Data
public class PopupNewsDTO {

    private String id;

    private String title;

    private String desc;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "GMT+7")
    private Date createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "GMT+7")
    private Date updatedDate;

    private String imageUrl;

    private String actionUrl;

    private Boolean isShowInApp;

    public PopupNewsDTO(PopupNews popupNews)
    {
        this.id = popupNews.getId();
        this.title = popupNews.getTitle();
        this.desc = popupNews.getDesc();
        this.content = popupNews.getContent();
        this.imageUrl = popupNews.getImageUrl();
        this.actionUrl = popupNews.getActionUrl();
        this.isShowInApp = popupNews.isShowInApp();
        this.createdDate = popupNews.getCreatedDate();
        this.updatedDate = popupNews.getUpdatedDate();
    }
}

