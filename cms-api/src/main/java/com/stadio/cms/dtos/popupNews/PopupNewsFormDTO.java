package com.stadio.cms.dtos.popupNews;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PopupNewsFormDTO {

    @NotNull
    private String title;

    @NotNull
    private String imageId;

    @NotNull
    private String url;

    @NotNull
    private String actionUrl;

    @NotNull
    private Boolean isShowInApp;

}
