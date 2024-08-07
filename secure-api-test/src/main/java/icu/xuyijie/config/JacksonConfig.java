package icu.xuyijie.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @author 徐一杰
 * @date 2022/11/29 23:56
 * @description 全局日期格式化，yml的jackson配置只对date类型生效，Calendar和LocalDateTime都无效，所以这里要写个配置文件
 */
@SpringBootConfiguration
@JsonComponent
public class JacksonConfig {
    private final Logger logger = LoggerFactory.getLogger(JacksonConfig.class);

    /**
     * 全局配置序列化返回 JSON 处理
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilder() {
        //可以手动设置时间格式和偏移量，下面会得到GMT+08:00
        //ZoneId zoneId = ZoneId.ofOffset("GMT", ZoneOffset.of("+8"))
        //可以自动获取系统时区，下面会得到Asia/Shanghai这样的格式
        //ZoneId zoneId = ZoneId.systemDefault()
        TimeZone tz = TimeZone.getTimeZone(ZoneId.systemDefault());
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        df.setTimeZone(tz);
        logger.info("设置全局日期格式化：{}--{}", pattern, tz.getID());

        return builder -> {
            builder.timeZone(TimeZone.getDefault());

            builder.failOnEmptyBeans(false)
                    .failOnUnknownProperties(false)
                    .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .dateFormat(df);

            builder
                    .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern)))
                    .serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));

            builder
                    .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(pattern)))
                    .deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        };
    }

}
