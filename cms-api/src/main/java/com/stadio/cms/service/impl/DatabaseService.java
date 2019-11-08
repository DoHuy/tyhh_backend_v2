package com.stadio.cms.service.impl;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IDatabaseService;
import com.stadio.model.repository.main.AchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class DatabaseService extends BaseController implements IDatabaseService {

    @Autowired
    AchievementRepository achievementRepository;

    @Override
    public ResponseResult processGetListCollection() {
        Set<String> databases = achievementRepository.getAllCollections();
        return ResponseResult.newSuccessInstance(databases);
    }
}
