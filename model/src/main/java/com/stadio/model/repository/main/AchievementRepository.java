package com.stadio.model.repository.main;

import com.stadio.model.documents.Achievement;
import com.stadio.model.enu.AchievementType;
import com.stadio.model.repository.main.custom.AchievementRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AchievementRepository extends MongoRepository<Achievement, String>, AchievementRepositoryCustom
{
    Achievement findByAchievementType(AchievementType achievementType);

}
