package com.stadio.model.repository.main;

import com.stadio.model.documents.PublicKey;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Andy on 03/02/2018.
 */
public interface PublicKeyRepository extends MongoRepository<PublicKey, String>
{
    PublicKey findByDeviceId(String deviceId);
}
