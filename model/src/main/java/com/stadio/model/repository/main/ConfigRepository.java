package com.stadio.model.repository.main;

import com.stadio.model.documents.Config;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigRepository extends MongoRepository<Config, String>
{
    Config findConfigByKey(String key);

}
