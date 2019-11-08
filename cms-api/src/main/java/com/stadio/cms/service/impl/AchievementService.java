package com.stadio.cms.service.impl;

import com.hoc68.users.documents.User;
import com.stadio.cms.dtos.AchievementQuery;
import com.stadio.cms.model.PageInfo;
import com.stadio.cms.response.TableList;
import com.stadio.common.utils.JsonUtils;
import com.stadio.common.utils.ResponseCode;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IAchievementService;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.*;import com.hoc68.users.documents.Manager;
import com.stadio.model.dtos.cms.AchievementFormDTO;
import com.stadio.model.dtos.cms.AchievementListDTO;
import com.stadio.model.dtos.cms.UserListDTO;
import com.stadio.model.repository.main.AchievementRepository;
import com.stadio.model.repository.main.ClazzRepository;
import com.stadio.model.repository.main.UserAchievementRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class AchievementService  extends BaseService implements IAchievementService {
    
    @Autowired
    AchievementRepository achievementRepository;

    @Autowired
    ClazzRepository clazzRepository;

    @Autowired
    UserAchievementRepository userAchievementRepository;

    Class<?>[] collections = {User.class, Rank.class, UserRank.class, UserExam.class};

    private Logger logger = LogManager.getLogger(AchievementService.class);

    @Override
    public ResponseResult processCreateOneAchievement(AchievementFormDTO achievementFormDTO) {
        ResponseResult responseResult = inValidAchievementForm(achievementFormDTO);
        if(responseResult!=null)
            return responseResult;

        Achievement achievement = new Achievement();
        achievement.setName(achievementFormDTO.getName());
        achievement.setDescription(achievementFormDTO.getDescription());
        achievement.setThumbnailUrl(achievementFormDTO.getThumbnailUrl());
        achievement.setCondition(achievementFormDTO.getCondition());
        achievement.setScheduleType(achievementFormDTO.getScheduleType());
        achievement.setCollectionName(achievementFormDTO.getCollectionName());

        achievementRepository.save(achievement);

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("achievement.success.create"),new AchievementListDTO(achievement));
    }

    @Override
    public ResponseResult ProcessUpdateOneAchievement(AchievementFormDTO achievementFormDTO) {
        ResponseResult responseResult = inValidAchievementForm(achievementFormDTO);
        if(responseResult!=null)
            return responseResult;
        if (!StringUtils.isNotNull(achievementFormDTO.getId()))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("achievement.invalid.id"), null);
        }

        Achievement achievement = achievementRepository.findOne(achievementFormDTO.getId());

        if (achievement == null)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("achievement.invalid.Achievement"), null);
        }

        achievement.setName(achievementFormDTO.getName());
        achievement.setDescription(achievementFormDTO.getDescription());
        achievement.setThumbnailUrl(achievementFormDTO.getThumbnailUrl());
        achievement.setCondition(achievementFormDTO.getCondition());
        achievement.setScheduleType(achievementFormDTO.getScheduleType());

        achievementRepository.save(achievement);

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("achievement.success.update"),new AchievementListDTO(achievement));
    }

    @Override
    public ResponseResult ProcessGetAchievementById(String id) {
        if (!StringUtils.isNotNull(id))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("achievement.invalid.id"), null);
        }
        Achievement achievement = achievementRepository.findOne(id);

        if(achievement==null||achievement.isDeleted())
            return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("achievement.failure.byId"),null);
        return ResponseResult.newInstance(ResponseCode.FILE_NOT_EXIST, getMessage("achievement.success.byId"),new AchievementListDTO(achievement));
    }

    @Override
    public ResponseResult ProcessGetAllAchievement() {
        List<Achievement> achievements = achievementRepository.findAll();
        if(achievements!=null){
            List<AchievementListDTO> achievementListDTOS = new ArrayList<>();
            achievements.forEach(achievement -> {
                if(!achievement.isDeleted())
                    achievementListDTOS.add(new AchievementListDTO(achievement));
            });
            return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("achievement.success.all"),achievementListDTOS);
        }
        else
            return ResponseResult.newInstance(ResponseCode.FILE_NOT_EXIST, getMessage("achievement.failure.all"),null);
    }

    @Override
    public ResponseResult ProcessDeleteAchievement(String id) {
        Achievement achievement = achievementRepository.findOne(id);
        if(achievement!=null){
            achievement.setDeleted(true);
            achievementRepository.save(achievement);
            return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("achievement.success.delete"),null);
        }else
            return ResponseResult.newInstance(ResponseCode.FILE_NOT_EXIST, getMessage("achievement.success.byId"),new AchievementListDTO(achievement));
    }

    @Override
    public ResponseResult achievementCheckResult(AchievementQuery achievementQuery) {
        if(!StringUtils.isNotNull(achievementQuery.getRawData())) {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("achievement.invalid.condition"), null);
        } else if (!StringUtils.isNotNull(achievementQuery.getCollectionName())) {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("achievement.invalid.collectionName"), null);
        }

        try {
            Object results = null;
            for (Class<?> ck: collections) {
                if (ck.isAnnotationPresent(Document.class)) {
                    Document doc = ck.getAnnotation(Document.class);
                    if (doc.collection().equals(achievementQuery.getCollectionName())) {
                        results = achievementRepository.achievementCheckResult(achievementQuery.getRawData(), ck);
                    }
                }
            }
            return ResponseResult.newSuccessInstance(JsonUtils.pretty(results));
        } catch (Exception e) {
            logger.error("achievementCheckResult: ", e);
        }

        return ResponseResult.newErrorInstance("01", this.getMessage("achievement.invalid.syntax"));
    }

    @Override
    public ResponseResult ProcessGetUserHaveAchievement(String id,Integer page,Integer pageSize) {
        List<UserAchievement> userAchievementList = this.userAchievementRepository.findByAchievementId(id, new PageRequest(page-1,pageSize,new Sort(Sort.Direction.DESC,"created_date"))).getContent();
        List<UserListDTO> userListDTOList = new LinkedList<>();
        if(userAchievementList!=null && userAchievementList.size()>0){
            userAchievementList.stream().forEach(userAchievement -> {
                String userId = userAchievement.getUserId();
                User user = userRepository.findOne(userId);
                if(user!=null)
                    userListDTOList.add(new UserListDTO(user,clazzRepository));
            });
        }

        Long count = this.userAchievementRepository.countByAchievementId(id);
        PageInfo pageInfo = new PageInfo(page,count , pageSize, "");
        TableList<UserListDTO> tableList = new TableList<>(pageInfo,userListDTOList);
        return ResponseResult.newSuccessInstance(tableList);
    }

    private ResponseResult inValidAchievementForm(AchievementFormDTO achievementFormDTO){
        if(achievementFormDTO==null)
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM,getMessage("achievement.invalid.Achievement"),null);
        if(!StringUtils.isNotNull(achievementFormDTO.getName()))
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM,getMessage("achievement.invalid.name"),null);
        if(!StringUtils.isNotNull(achievementFormDTO.getDescription()))
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM,getMessage("achievement.invalid.description"),null);
        if(!StringUtils.isNotNull(achievementFormDTO.getCondition()))
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM,getMessage("achievement.invalid.condition"),null);
        return null;
    }
    
    
}
