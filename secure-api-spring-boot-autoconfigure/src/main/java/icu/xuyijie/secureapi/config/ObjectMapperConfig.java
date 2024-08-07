package icu.xuyijie.secureapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;

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
 * @description 由于各类工具对LocalDateTime的支持性不好（如redis），所以我们需要配置时间序列化，使用时new JacksonConfig.myObjectMapper()传入redisson序列化器就行了
 */
public class ObjectMapperConfig {
    private final SecureApiPropertiesConfig secureApiPropertiesConfig;

    public ObjectMapperConfig(SecureApiPropertiesConfig secureApiPropertiesConfig) {
        this.secureApiPropertiesConfig = secureApiPropertiesConfig;
    }

    public ObjectMapper myObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // 这里是对Date类型进行格式化设置
        TimeZone tz = TimeZone.getTimeZone(ZoneId.systemDefault());
        DateFormat df = new SimpleDateFormat(secureApiPropertiesConfig.getDateFormat());
        df.setTimeZone(tz);
        objectMapper.setDateFormat(df);

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        //序列化
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(secureApiPropertiesConfig.getLocalDateTimeFormat())));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(secureApiPropertiesConfig.getLocalDateFormat())));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(secureApiPropertiesConfig.getLocalTimeFormat())));

        //反序列
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(secureApiPropertiesConfig.getLocalDateTimeFormat())));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(secureApiPropertiesConfig.getLocalDateFormat())));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(secureApiPropertiesConfig.getLocalTimeFormat())));

        //注册模块
        objectMapper.registerModule(javaTimeModule);

        return objectMapper;
    }
}
