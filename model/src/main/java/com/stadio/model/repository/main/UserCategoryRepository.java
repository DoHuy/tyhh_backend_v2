package com.stadio.model.repository.main;

import com.stadio.model.documents.UserCategory;
import com.stadio.model.enu.UserCategoryAction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserCategoryRepository extends MongoRepository<UserCategory, String> {

    UserCategory findFirstByUserIdRefAndCategoryIdRefAndAction(String userId, String categoryId, UserCategoryAction action);

    List<UserCategory> findAllByUserIdRefAndAction(String userId, UserCategoryAction action);

    long countByUserIdRefAndCategoryIdRefAndAction(String userId, String categoryId, UserCategoryAction action);

    long countByCategoryIdRefAndAction(String categoryId, UserCategoryAction action);

}
