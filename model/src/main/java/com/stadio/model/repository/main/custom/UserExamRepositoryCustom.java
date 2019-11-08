package com.stadio.model.repository.main.custom;

import com.mongodb.DBObject;
import com.stadio.model.documents.UserExam;

import java.util.List;

/**
 * Created by Andy on 02/28/2018.
 */
public interface UserExamRepositoryCustom
{
    Iterable<DBObject> groupByCorrectNumber(String examId);
}
