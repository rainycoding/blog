package com.ktz.blog.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.AttributeConverter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by 曾泉明 on 2020/2/24 22:14
 */
@Configuration
public class LocalDateTimeConverter {

    @Bean
    public AttributeConverter<LocalDateTime, Timestamp> attributeConverter() {
        return new AttributeConverter<LocalDateTime, Timestamp>() {
            @Override
            public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
                return localDateTime == null ? null : Timestamp.valueOf(localDateTime);
            }

            @Override
            public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
                return timestamp == null ? null : timestamp.toLocalDateTime();
            }
        };
    }

//    @Bean
//    public LocalDateTimeSerializer localDateTimeSerializer() {
//        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//    }
//
//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
//        return builder -> builder.serializerByType(LocalDateTime.class, localDateTimeSerializer());
//    }
}
