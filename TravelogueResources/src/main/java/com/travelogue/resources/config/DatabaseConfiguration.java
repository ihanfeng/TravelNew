package com.travelogue.resources.config;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.github.mongobee.Mongobee;
import com.mongodb.Mongo;
import com.travelogue.resources.domain.util.JSR310DateConverters.DateToLocalDateConverter;
import com.travelogue.resources.domain.util.JSR310DateConverters.DateToLocalDateTimeConverter;
import com.travelogue.resources.domain.util.JSR310DateConverters.DateToZonedDateTimeConverter;
import com.travelogue.resources.domain.util.JSR310DateConverters.LocalDateTimeToDateConverter;
import com.travelogue.resources.domain.util.JSR310DateConverters.LocalDateToDateConverter;
import com.travelogue.resources.domain.util.JSR310DateConverters.ZonedDateTimeToDateConverter;


@Configuration
@Profile("!" + Constants.SPRING_PROFILE_CLOUD)
@EnableMongoRepositories("com.travelogue.resources.repository")
@Import(value = MongoAutoConfiguration.class)
@EnableMongoAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class DatabaseConfiguration extends AbstractMongoConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Inject
    private Mongo mongo;

    @Inject
    private MongoProperties mongoProperties;

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener() {
        return new ValidatingMongoEventListener(validator());
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Override
    protected String getDatabaseName() {
        return mongoProperties.getDatabase();
    }

    @Override
    public Mongo mongo() throws Exception {
        return mongo;
    }

    @Bean
    public CustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(DateToZonedDateTimeConverter.INSTANCE);
        converters.add(ZonedDateTimeToDateConverter.INSTANCE);
        converters.add(DateToLocalDateConverter.INSTANCE);
        converters.add(LocalDateToDateConverter.INSTANCE);
        converters.add(DateToLocalDateTimeConverter.INSTANCE);
        converters.add(LocalDateTimeToDateConverter.INSTANCE);
        return new CustomConversions(converters);
    }

    @Bean
    public Mongobee mongobee() {
        log.debug("Configuring Mongobee");
        Mongobee mongobee = new Mongobee(mongo);
        mongobee.setDbName(mongoProperties.getDatabase());
        // package to scan for migrations
        mongobee.setChangeLogsScanPackage("com.travelogue.resources.config.dbmigrations");
        mongobee.setEnabled(true);
        return mongobee;
    }
    
    @Bean
	public GridFsTemplate gridFsTemplate() throws Exception {
		return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
	}
}
