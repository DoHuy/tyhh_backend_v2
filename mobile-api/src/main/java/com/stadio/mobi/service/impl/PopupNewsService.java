package com.stadio.mobi.service.impl;
import com.stadio.mobi.dtos.popupNews.PopupNewsDTO;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IPopupNewsService;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.repository.main.PopupNewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PopupNewsService extends BaseService implements IPopupNewsService {

    @Autowired
    PopupNewsRepository popupNewsRepository;

    @Override
    public ResponseResult getListPopupNews() {
        List<PopupNews> popupNewsShowed = popupNewsRepository.findAllByShowInApp(true);
        List<PopupNewsDTO> popupNewsDTOList = new ArrayList<>();
        for (PopupNews popupNews: popupNewsShowed) {
            PopupNewsDTO popupNewsDTO = new PopupNewsDTO(popupNews);
            popupNewsDTOList.add(popupNewsDTO);
        }
        return ResponseResult.newSuccessInstance(popupNewsDTOList);
    }

}
