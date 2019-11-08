package com.stadio.cms.service;

import com.stadio.cms.dtos.popupNews.PopupNewsFormDTO;
import com.stadio.cms.response.ResponseResult;

public interface IPopupNewsService {

    public ResponseResult getListPopupNews();

    public ResponseResult addNewPopupNews(PopupNewsFormDTO popupNewsFormDTO);

    public ResponseResult hidePopupNews(String id);

    public ResponseResult deletePopupNews(String id);

    public ResponseResult makePopupNewsShowInApp(String id);

}
