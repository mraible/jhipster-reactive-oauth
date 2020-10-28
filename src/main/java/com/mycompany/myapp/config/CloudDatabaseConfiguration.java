package com.mycompany.myapp.config;

import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.SpringDataMongo3Driver;
import com.github.cloudyrock.spring.v5.MongockSpring5;
import com.github.cloudyrock.spring.v5.MongockSpring5.MongockInitializingBeanRunner;
import com.mycompany.myapp.GeneratedByJHipster;
import io.github.jhipster.config.JHipsterConstants;
import io.github.jhipster.domain.util.JSR310DateConverters.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableReactiveMongoRepositories("com.mycompany.myapp.repository")
@Profile(JHipsterConstants.SPRING_PROFILE_CLOUD)
@GeneratedByJHipster
public class CloudDatabaseConfiguration extends AbstractCloudConfig {

    private final Logger log = LoggerFactory.getLogger(CloudDatabaseConfiguration.class);

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener() {
        return new ValidatingMongoEventListener(validator());
    }

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<>();
        converterList.add(DateToZonedDateTimeConverter.INSTANCE);
        converterList.add(ZonedDateTimeToDateConverter.INSTANCE);
        converterList.add(DurationToLongConverter.INSTANCE);
        return new MongoCustomConversions(converterList);
    }

    @Bean
    public MongockInitializingBeanRunner mongockInitializingBeanRunner(
        ApplicationContext springContext,
        MongoTemplate mongoTemplate,
        @Value("${mongock.lockAcquiredForMinutes:5}") long lockAcquiredForMinutes,
        @Value("${mongock.maxWaitingForLockMinutes:3}") long maxWaitingForLockMinutes,
        @Value("${mongock.maxTries:3}") int maxTries
    ) {
        SpringDataMongo3Driver driver = SpringDataMongo3Driver.withLockSetting(
            mongoTemplate,
            lockAcquiredForMinutes,
            maxWaitingForLockMinutes,
            maxTries
        );
        return MongockSpring5
            .builder()
            .setDriver(driver)
            .addChangeLogsScanPackage("com.mycompany.myapp.config.dbmigrations")
            .setSpringContext(springContext)
            .buildInitializingBeanRunner();
    }
}
