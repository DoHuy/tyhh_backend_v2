package com.stadio.model.repository.main;

import com.stadio.model.documents.RechargeCardExport;
import com.stadio.model.repository.main.custom.RechargeCardExportRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RechargeCardExportRepository extends MongoRepository<RechargeCardExport, String>, RechargeCardExportRepositoryCustom {

}
