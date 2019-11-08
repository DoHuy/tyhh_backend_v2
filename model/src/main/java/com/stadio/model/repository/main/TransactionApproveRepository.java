package com.stadio.model.repository.main;

import com.stadio.model.documents.TransactionApprove;
import com.stadio.model.repository.main.custom.TransactionApproveRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionApproveRepository extends MongoRepository<TransactionApprove, String>, TransactionApproveRepositoryCustom {


}
