package cn.piesat.framework.mybatis.plus.config;

import cn.piesat.framework.mybatis.plus.properties.MybatisPlusConfigProperties;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(MybatisPlusConfigProperties.class)
@RequiredArgsConstructor
@ConditionalOnProperty(name = "space.db.date-format-enable", havingValue = "true",matchIfMissing = false)
public class LocalDateTimeSerializerConfig {

    private final MybatisPlusConfigProperties mybatisPlusConfigProperties;

    @Bean
    @ConditionalOnMissingBean
    public Jackson2ObjectMapperBuilder mapperBuilder() {
        Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = new Jackson2ObjectMapperBuilder();
        return jackson2ObjectMapperBuilder.dateFormat(new SimpleDateFormat(mybatisPlusConfigProperties.getDateFormat()))
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(mybatisPlusConfigProperties.getDateFormat())))
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(mybatisPlusConfigProperties.getDateFormat())));
    }
}
