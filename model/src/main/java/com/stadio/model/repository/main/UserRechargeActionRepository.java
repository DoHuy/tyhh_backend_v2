package com.stadio.model.repository.main;

import com.stadio.model.documents.UserRechargeAction;
import com.stadio.model.repository.main.custom.UserRechargeActionRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRechargeActionRepository extends MongoRepository<UserRechargeAction, String>, UserRechargeActionRepositoryCustom {

}
