package com.stadio.model.repository.main;

import com.stadio.model.documents.Question;
import com.stadio.model.repository.main.custom.QuestionRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Andy on 11/10/2017.
 */
public interface QuestionRepository extends MongoRepository<Question, String>, QuestionRepositoryCustom
{

}
