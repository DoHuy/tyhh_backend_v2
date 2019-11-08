package com.stadio.model.dbconfig;


import com.mongodb.MongoClient;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.stadio.common.utils.StringUtils;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MultipleMongoProperties.class)
@Import({PrimaryMongoConfig.class, SecondaryMongoConfig.class})
public class MultipleMongoConfig {

    private final MultipleMongoProperties mongoProperties;

    @Primary
    @Bean(name = "primaryMongoTemplate")
    public MongoTemplate primaryMongoTemplate() throws Exception {
        return new MongoTemplate(primaryFactory(this.mongoProperties.getPrimary()));
    }

    @Bean(name = "secondaryMongoTemplate")
    public MongoTemplate secondaryMongoTemplate() throws Exception {
        MongoDbFactory mongoDbFactory = secondaryFactory(this.mongoProperties.getSecondary());
//        if (mongoDbFactory == null) {
//            return this.primaryMongoTemplate();
//        }
        return new MongoTemplate(mongoDbFactory);
    }

    @Bean
    @Primary
    public MongoDbFactory primaryFactory(final MongoProperties mongo) throws Exception {
        List<MongoCredential> credentials = null;
        if (mongo.getUsername() != null || mongo.getPassword() != null) {
            credentials = Arrays.asList(MongoCredential.createCredential(mongo.getUsername(), mongo.getDatabase(), mongo.getPassword()));
        }
        MongoClient mongoClient = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getPort()), credentials);
        return new SimpleMongoDbFactory(mongoClient, mongo.getDatabase());
    }

    @Bean
    public MongoDbFactory secondaryFactory(final MongoProperties mongo) throws Exception {
        if (StringUtils.isNotNull(mongo.getDatabase()) & StringUtils.isNotNull(mongo.getHost())) {
            List<MongoCredential> credentials = null;
            if (mongo.getUsername() != null || mongo.getPassword() != null) {
                credentials = Arrays.asList(MongoCredential.createCredential(mongo.getUsername(), mongo.getDatabase(), mongo.getPassword()));
            }
            MongoClient mongoClient = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getPort()), credentials);
            return new SimpleMongoDbFactory(mongoClient, mongo.getDatabase());
        } else {
            return primaryFactory(this.mongoProperties.getPrimary());

        }
    }

}