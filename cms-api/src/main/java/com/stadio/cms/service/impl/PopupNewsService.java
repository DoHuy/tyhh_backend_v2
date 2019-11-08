package com.stadio.cms.service.impl;

import com.stadio.cms.dtos.popupNews.PopupNewsDTO;
import com.stadio.cms.dtos.popupNews.PopupNewsFormDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IPopupNewsService;
import com.stadio.common.utils.ResponseCode;
import com.stadio.model.documents.PopupNews;
import com.stadio.model.repository.main.PopupNewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PopupNewsService extends BaseService implements IPopupNewsService {

    @Autowired
    PopupNewsRepository popupNewsRepository;

    @Override
    public ResponseResult getListPopupNews() {

        List<PopupNews> popupNewsShowed = popupNewsRepository.findAllByShowInAppSorted();
        List<PopupNewsDTO> popupNewsDTOList = new ArrayList<>();
        for (PopupNews popupNews: popupNewsShowed) {
            PopupNewsDTO popupNewsDTO = new PopupNewsDTO(popupNews);
            popupNewsDTOList.add(popupNewsDTO);
        }
        return ResponseResult.newSuccessInstance(popupNewsDTOList);
    }

    @Override
    public ResponseResult addNewPopupNews(PopupNewsFormDTO popupNewsFormDTO) {

        if (popupNewsFormDTO.getIsShowInApp()) {
            this.disableOtherPopupNews();
        }

        PopupNews popupNews = new PopupNews();
        popupNews.setTitle(popupNewsFormDTO.getTitle());
        popupNews.setActionUrl(popupNewsFormDTO.getActionUrl());
        popupNews.setImageUrl(popupNewsFormDTO.getUrl());
        popupNews.setImageIdRef(popupNewsFormDTO.getImageId());
        popupNews.setShowInApp(popupNewsFormDTO.getIsShowInApp());

        popupNewsRepository.save(popupNews);

        return ResponseResult.newSuccessInstance(new PopupNewsDTO(popupNews));
    }

    @Override
    public ResponseResult hidePopupNews(String id) {
        PopupNews popupNews = popupNewsRepository.findOne(id);
        if (popupNews == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, this.getMessage("popup.news.not.found"));
        }

        popupNews.setShowInApp(false);

        popupNewsRepository.save(popupNews);
        return ResponseResult.newSuccessInstance(null);
    }

    @Override
    public ResponseResult deletePopupNews(String id) {
        if (popupNewsRepository.findOne(id) == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, this.getMessage("popup.news.not.found"));
        }
        popupNewsRepository.delete(id);
        return ResponseResult.newSuccessInstance(null);
    }

    @Override
    public ResponseResult makePopupNewsShowInApp(String id) {
        PopupNews popupNews = popupNewsRepository.findOne(id);
        if (popupNews == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, this.getMessage("popup.news.not.found"));
        }
        //make other
        this.disableOtherPopupNews();
        popupNews.setShowInApp(true);
        popupNews.setUpdatedDate(new Date());
        popupNewsRepository.save(popupNews);
        return ResponseResult.newSuccessInstance(new PopupNewsDTO(popupNews));
    }

    private void disableOtherPopupNews() {
        List<PopupNews> popupNewsShowed = popupNewsRepository.findAllByShowInApp(true);
        for (PopupNews popupNews: popupNewsShowed) {
            if (popupNews.isShowInApp()) {
                popupNews.setShowInApp(false);
                popupNews.setUpdatedDate(new Date());
                popupNewsRepository.save(popupNews);
            }
        }
    }

}
