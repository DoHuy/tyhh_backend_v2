package com.stadio.cms;

import com.stadio.cms.service.impl.DocumentService;
import com.stadio.common.service.impl.StorageService;
import com.stadio.model.redisUtils.RedisRepository;
import com.stadio.model.redisUtils.RedisRepositoryImpl;
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
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * Created by Andy on 02/16/2018.
 */
@SpringBootApplication(scanBasePackages = {"com.stadio"}, exclude = {RedisAutoConfiguration.class})
@EnableTransactionManagement

//Config to use com.stadio.moel.dbconfig, uncomment this will switch to default mongo spring config
@ComponentScan(basePackages = {"com.stadio"})
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class,
        MongoRepositoriesAutoConfiguration.class, EmbeddedMongoAutoConfiguration.class
})
@EnableAsync(proxyTargetClass = true)
public class CMSApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CMSApplication.class, args);
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        return loggingFilter;
    }

    @Bean
    public CommandLineRunner init(StorageService storageService, DocumentService documentService) {
        return (args) -> {
            documentService.processUpdateListFeatureToDB();
            storageService.init();
        };
    }

    @Autowired
    RedisTemplate redisTemplate;

    @Bean
    public RedisRepository redisRepository() {
        return new RedisRepositoryImpl(redisTemplate);
    }
}
