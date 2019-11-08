package com.stadio.model.repository.main.custom;

import com.mongodb.DBObject;

public interface UserPointRepositoryCustom {

    Iterable<DBObject> groupPointByUser(Integer year, Integer month);
}
