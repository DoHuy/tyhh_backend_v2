package com.stadio.model.repository.main;

import com.stadio.model.documents.CatchCharging;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Andy on 03/03/2018.
 */
public interface CatchChargingRepository extends MongoRepository<CatchCharging, String>
{

}
