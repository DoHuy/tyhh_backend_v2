package com.stadio.model.dbconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.stadio.model.repository.main", "com.stadio.model.repository.chemistry"},
        mongoTemplateRef = "primaryMongoTemplate")
public class PrimaryMongoConfig {

}