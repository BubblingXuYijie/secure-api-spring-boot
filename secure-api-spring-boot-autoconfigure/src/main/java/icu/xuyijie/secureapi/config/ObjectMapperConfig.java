package icu.xuyijie.secureapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

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
 * @date 2023/2/27 16:06
 * @description json序列化配置
 */
public class ObjectMapperConfig {
    private final SecureApiPropertiesConfig secureApiPropertiesConfig;
    private final Jackson2ObjectMapperBuilder builder;

    public ObjectMapperConfig(SecureApiPropertiesConfig secureApiPropertiesConfig, Jackson2ObjectMapperBuilder builder) {
        this.secureApiPropertiesConfig = secureApiPropertiesConfig;
        this.builder = builder;
    }

    public ObjectMapper myObjectMapper() {
        // 这里是对Date类型进行格式化设置
        TimeZone tz = TimeZone.getTimeZone(ZoneId.systemDefault());
        DateFormat df = new SimpleDateFormat(secureApiPropertiesConfig.getDateFormat());
        df.setTimeZone(tz);
        builder.dateFormat(df);

        // LocalDateTime序列化
        builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(secureApiPropertiesConfig.getLocalDateTimeFormat())));
        builder.serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(secureApiPropertiesConfig.getLocalDateFormat())));
        builder.serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(secureApiPropertiesConfig.getLocalTimeFormat())));
        // LocalDateTime反序列化
        builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(secureApiPropertiesConfig.getLocalDateTimeFormat())));
        builder.deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(secureApiPropertiesConfig.getLocalDateFormat())));
        builder.deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(secureApiPropertiesConfig.getLocalTimeFormat())));

        // 时间类型转换为 string 后以时间格式输出，而不是毫秒值
        builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return builder.build();
    }
}
