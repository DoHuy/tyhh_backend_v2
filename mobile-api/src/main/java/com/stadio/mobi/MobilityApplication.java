package com.stadio.mobi;

import com.stadio.common.define.Constant;
import com.stadio.common.service.impl.StorageService;
import com.stadio.mobi.service.IMigrateDBService;
import com.stadio.model.redisUtils.RedisRepository;
import com.stadio.model.redisUtils.RedisRepositoryImpl;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 02/17/2018.
 */
@SpringBootApplication(scanBasePackages = {"com.stadio"}, exclude = { RedisAutoConfiguration.class})
@EnableTransactionManagement

//Config to use com.stadio.moel.dbconfig, uncomment this will switch to default mongo spring config
@ComponentScan(basePackages = {"com.stadio"})
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class,
        MongoRepositoriesAutoConfiguration.class, EmbeddedMongoAutoConfiguration.class
})
public class MobilityApplication extends SpringBootServletInitializer {

    public static void main(String[] args)
    {
        SpringApplication.run(MobilityApplication.class, args);
    }

    @Autowired
    IMigrateDBService migrateDBService;

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public CommandLineRunner init(StorageService storageService) {
        migrateDBService.runMigrate();
        return (args) -> storageService.init();
    }

    @Autowired
    RedisTemplate redisTemplate;

    @Bean
    public RedisRepository redisRepository() {
        return new RedisRepositoryImpl(redisTemplate);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.stadio.mobi.controllers"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public TopicExchange exchangeNotification() {
        return new TopicExchange(Constant.EXCHANGE_NAME.NOTIFICATION);
    }

}
