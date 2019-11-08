package com.stadio.model.repository.main;

import com.stadio.model.documents.UserAchievement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserAchievementRepository extends MongoRepository<UserAchievement, String> {

    UserAchievement findByUserIdAndAchievementId(String userId, String achievementId);

    List<UserAchievement> findByUserId(String userId);

    Page<UserAchievement> findByAchievementId(String id, Pageable pageable);

    Long countByAchievementId(String id);
}
