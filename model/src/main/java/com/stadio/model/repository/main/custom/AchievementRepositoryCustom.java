package com.stadio.model.repository.main.custom;

import java.util.List;
import java.util.Set;

public interface AchievementRepositoryCustom
{
    <T> List<T> achievementCheckResult(String raw, Class<T> ck) throws Exception;

    Set<String> getAllCollections();
}
