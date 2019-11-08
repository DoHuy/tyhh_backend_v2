package com.stadio.model.repository.main.custom;

import com.stadio.model.documents.PopupNews;

import java.util.List;

public interface PopupNewsRepositoryCustom {

    public List<PopupNews> findAllByShowInAppSorted();

}
