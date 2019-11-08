package com.stadio.cms.service;

import com.stadio.cms.dtos.AchievementQuery;
import com.stadio.cms.response.ResponseResult;
import com.stadio.model.dtos.cms.AchievementFormDTO;

public interface IAchievementService {
    ResponseResult processCreateOneAchievement(AchievementFormDTO achievementFormDTO);

    ResponseResult ProcessUpdateOneAchievement(AchievementFormDTO achievementFormDTO);

    ResponseResult ProcessGetAchievementById(String id);

    ResponseResult ProcessGetAllAchievement();

    ResponseResult ProcessDeleteAchievement(String id);

    ResponseResult achievementCheckResult(AchievementQuery achievementQuery);

    ResponseResult ProcessGetUserHaveAchievement(String id,Integer page,Integer pageSize);
}
