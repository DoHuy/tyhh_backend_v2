package com.stadio.model.repository.main.custom;

import com.stadio.model.documents.Clazz;

import java.util.List;

public interface ClazzRepositoryCustom {

    Clazz findOneById(String id);

    List<Clazz> findClazzRelateToUserId(String userId);

}
