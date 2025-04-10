package icu.xuyijie.secureapi.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import icu.xuyijie.secureapi.annotation.DecryptIgnore;
import icu.xuyijie.secureapi.annotation.DecryptParam;
import icu.xuyijie.secureapi.config.ThreadPoolConfig;
import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;
import icu.xuyijie.secureapi.threadlocal.SecureApiThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author 徐一杰
 * @date 2024/6/24 19:14
 * @description param和formData参数处理器
 */
@Component
public class SecureApiArgumentResolver implements HandlerMethodArgumentResolver {
    private final Logger log = LoggerFactory.getLogger(SecureApiArgumentResolver.class);
    private final ThreadPoolTaskExecutor threadPool = ThreadPoolConfig.secureThreadPool();

    private final SecureApiPropertiesConfig secureApiPropertiesConfig;
    private final ObjectMapper secureApiObjectMapper;
    private final List<DateTimeFormatter> dateFormatterList;

    public SecureApiArgumentResolver(SecureApiPropertiesConfig secureApiPropertiesConfig, ObjectMapper secureApiObjectMapper) {
        this.secureApiPropertiesConfig = secureApiPropertiesConfig;
        this.secureApiObjectMapper = secureApiObjectMapper;
        dateFormatterList = Arrays.asList(
                DateTimeFormatter.ofPattern(this.secureApiPropertiesConfig.getDateFormat()),
                DateTimeFormatter.ofPattern(this.secureApiPropertiesConfig.getLocalDateTimeFormat()),
                DateTimeFormatter.ofPattern(this.secureApiPropertiesConfig.getLocalDateFormat()),
                DateTimeFormatter.ofPattern(this.secureApiPropertiesConfig.getLocalTimeFormat()),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy-MM"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("yyyy/MM"),
                DateTimeFormatter.ofPattern("yyyyMMdd"),
                DateTimeFormatter.ofPattern("yyyyMM"),
                DateTimeFormatter.ofPattern("yyyy年MM月"),
                DateTimeFormatter.ofPattern("yyyy年MM月dd日"),
                DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm"),
                DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss.SSS"),
                DateTimeFormatter.ofPattern("HH:mm:ss"),
                DateTimeFormatter.ofPattern("HH:mm:ss a"),
                DateTimeFormatter.ofPattern("HH:mm:ss.SSS"),
                DateTimeFormatter.ofPattern("HH:mm:ss.SSS a"),
                DateTimeFormatter.ofPattern("HH:mm:ss.S"),
                DateTimeFormatter.ofPattern("HH:mm:ss.S a"),
                DateTimeFormatter.ofPattern("HH:mm"),
                DateTimeFormatter.ofPattern("HH:mm a"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssz"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
                DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy"),
                DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US),
                DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z"),
                DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US),
                // 2024-08-06T09:32:02
                DateTimeFormatter.ISO_DATE_TIME,
                DateTimeFormatter.ISO_DATE,
                DateTimeFormatter.ISO_TIME,
                // 2024-08-06T09:32:02+00:00
                DateTimeFormatter.ISO_OFFSET_DATE_TIME,
                DateTimeFormatter.ISO_OFFSET_DATE,
                DateTimeFormatter.ISO_OFFSET_TIME,
                DateTimeFormatter.ISO_ORDINAL_DATE,
                // 2024-08-06T09:32:02Z[UTC]
                DateTimeFormatter.ISO_ZONED_DATE_TIME,
                DateTimeFormatter.BASIC_ISO_DATE,
                DateTimeFormatter.ISO_WEEK_DATE,
                DateTimeFormatter.ISO_LOCAL_DATE,
                DateTimeFormatter.ISO_LOCAL_TIME,
                DateTimeFormatter.ISO_LOCAL_DATE_TIME,
                DateTimeFormatter.RFC_1123_DATE_TIME,
                DateTimeFormatter.ISO_INSTANT
        );
    }

    @Override
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        if (secureApiPropertiesConfig.isEnabled()) {
            boolean parameterAnnotation = parameter.hasParameterAnnotation(DecryptParam.class);
            // 获取参数类型
            Class<?> parameterType = parameter.getParameterType();
            // 根据参数类型，获取对象，如果对象为空，可能是实体类，放行，下面会检测实体类字段哪些需要解密
            Object o = getObjectByType(parameterType, "");
            // 四种情况走这个处理器：1、配置了解密url并参数没有加@RequestParam、@RequestPart注解。2、参数加了@DecryptParam注解。3、参数是实体类。4、传值为null(按需处理defaultValue)
            return SecureApiThreadLocal.getIsDecryptApi() || parameterAnnotation || o == null;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String parameterName = parameter.getParameterName();
        // 极端情况
        if (!StringUtils.hasText(parameterName)) {
            return null;
        }
        DecryptParam decryptParam = parameter.getParameterAnnotation(DecryptParam.class);
        boolean hasDecryptParam = decryptParam != null;
        // 存在注解重新设置参数名
        if (hasDecryptParam && StringUtils.hasText(decryptParam.value())) {
            parameterName = decryptParam.value();
        }
        // 得到参数参数值，因为前端传来的密文一定是字符串，所以这里就不需要做类型处理了，类型处理在解密以后
        String encryptParam = webRequest.getParameter(parameterName);
        String parameterValue = encryptParam;
        // 获取参数类型
        Class<?> parameterType = parameter.getParameterType();
        // 参数值为null并且DecryptParam注解要求参数必传并且没有设置defaultValue，抛出异常
        if (parameterValue == null && hasDecryptParam && decryptParam.required() && !StringUtils.hasText(decryptParam.defaultValue())) {
            throw new MissingServletRequestParameterException(decryptParam.value(), parameterType.getTypeName());
        }
        // 解密参数值，这里再判断一次是应对实体类作为参数的情况，参数是实体类时 @DecryptParam 注解在字段上，无法在 supportsParameter 方法中判断，只能进入到这里判断，如果用户很傻在实体类参数上加了 @DecryptParam 也没关系，不会报错
        boolean isDecryptParam = SecureApiThreadLocal.getIsDecryptApi() && !parameter.hasParameterAnnotation(DecryptIgnore.class) || hasDecryptParam;
        if (isDecryptParam) {
            parameterValue = CipherModeHandler.handleDecryptMode(parameterValue, secureApiPropertiesConfig);
        }
        // 参数不为空并且解密成功，解密后要自行处理各种类型
        Object result = null;
        if (parameterValue != null) {
            // 特定参数类型需要处理成对象，不需要处理的类型保持原值
            result = getObjectByType(parameterType, parameterValue);
            if (result == null) {
                result = parameterValue;
            }
            showLog(parameterName, encryptParam, parameterValue);
        } else {
            // 自定义的实体类类型或者传入值为null的情况
            try {
                // 实例化对象
                result = parameterType.getDeclaredConstructor().newInstance();
                // 递归获取对象内字段
                getAllFields(webRequest, parameterType, result);
            } catch (Exception e) {
                // 参数解密失败或者参数不是实体类而是为null不解密返回defaultValue
                if (hasDecryptParam && StringUtils.hasText(decryptParam.defaultValue())) {
                    result = decryptParam.defaultValue();
                }
            }
        }

        // springboot处理参数类型
        if (binderFactory != null) {
            WebDataBinder binder = binderFactory.createBinder(webRequest, null, parameterName);
            try {
                result = binder.convertIfNecessary(result, parameter.getParameterType(), parameter);
            } catch (ConversionNotSupportedException ex) {
                throw new MethodArgumentConversionNotSupportedException(result, ex.getRequiredType(), parameterName, parameter, ex.getCause());
            } catch (TypeMismatchException ex) {
                throw new MethodArgumentTypeMismatchException(result, ex.getRequiredType(), parameterName, parameter, ex.getCause());
            }
        }

        return result;
    }

    /**
     * 把List进行toString后的字符串转回List
     *
     * @param s List进行toString后的字符串
     * @return 转回List数组
     */
    private List<String> handleListString(String s) {
        try {
            // 判断是否是合法的list字符串
            if (s.length() > 1 && s.charAt(0) == '[' && s.charAt(s.length() - 1) == ']') {
                // 去除字符串开头和结尾的[]
                s = s.substring(0, s.length() - 1).replaceFirst("\\[", "");
                // 如果不是空list
                if (StringUtils.hasText(s)) {
                    // 如果字符串是list.toString转换出来的，那么逗号后面会有一个空格，还会用引号包裹每个元素，都要把它们去掉
                    if (s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"' && (s.contains("\", \"") || s.contains("\",\""))) {
                        s = s.substring(0, s.length() - 1).replaceFirst("\"", "").replace("\", \"", ",").replace("\",\"", ",");
                    } else {
                        s = s.replace(", ", ",");
                    }
                    return Arrays.asList(s.split(","));
                }
            }
        } catch (Exception e) {
            try {
                return secureApiObjectMapper.readValue(s, new TypeReference<List<String>>() {});
            } catch (JsonProcessingException ex) {
                log.error("非法的List字符串，无法反序列化为list：{}", s, ex);
            }
        }
        return Collections.emptyList();
    }

    /**
     * 把字符串转换为对应类型数据，如果没有对应类型，返回null
     *
     * @param parameterType 对应类型
     * @param parameterValue 字符串
     * @return 字符串转换为对应类型数据，如果没有对应类型，返回null
     */
    private Object getObjectByType(Class<?> parameterType, String parameterValue) {
        if (parameterValue == null) {
            return null;
        }
        Object o = null;
        if (List.class.isAssignableFrom(parameterType)) {
            o = new ArrayList<>(handleListString(parameterValue));
        } else if (parameterType.isArray()) {
            o = handleListString(parameterValue);
        } else if (Set.class.isAssignableFrom(parameterType)) {
            o = new HashSet<>(handleListString(parameterValue));
        } else if (Queue.class.isAssignableFrom(parameterType)) {
            o = new PriorityQueue<>(handleListString(parameterValue));
        } else if (Map.class.isAssignableFrom(parameterType)) {
            try {
                // 这种情况适用于 map.toString() 方法转换的字符串形式：["a=哈哈", "b=嘿嘿"]
                // 去除可能因数据转换过程中产生的收尾多余的双引号、大括号、空格
                String s = handleHeadAndTailQuotationMarks(parameterValue);
                if (s.length() > 1 && s.charAt(0) == '{' && s.charAt(s.length() - 1) == '}') {
                    s = s.substring(1, s.length() - 1);
                }
                String[] kvs = s.split(",");
                if (kvs.length > 0 && StringUtils.hasText(kvs[0])) {
                    Map<String, String> map = new HashMap<>(kvs.length);
                    for (String kv : kvs) {
                        String[] split = kv.split("=");
                        map.put(split[0].replace(" ", ""), split[1]);
                    }
                    o = map;
                } else {
                    o = new HashMap<>(0);
                }
            } catch (Exception e) {
                try {
                    // 去除可能因数据转换过程中产生的收尾多余的双引号
                    String s = handleHeadAndTailQuotationMarks(parameterValue);
                    o = secureApiObjectMapper.readValue(s, Map.class);
                } catch (JsonProcessingException ex) {
                    log.error("参数值：{} 不符合map格式，转换失败", parameterValue, ex);
                }
            }
        }
        return o;
    }

    /**
     * 去除可能因数据转换过程中产生的收尾多余的双引号
     *
     * @param s 原始字符串
     * @return 去除了头部和尾部双引号的字符串
     */
    private String handleHeadAndTailQuotationMarks(String s) {
        if (s.length() > 1 && s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') {
            s = s.substring(1, s.length() - 1);
        }
        return s;
    }

    /**
     * 递归获取所有父类字段
     * @param webRequest 请求信息
     * @param parameterType 实体类参数类型
     * @param result 实体类实例
     */
    private void getAllFields(WebRequest webRequest, Class<?> parameterType, Object result) {
        // 实体类字段一般会比较多，采用异步编排提高性能
        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();
        Class<?> clazz = parameterType;
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                completableFutureList.add(CompletableFuture.runAsync(() -> {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    try {
                        if (Modifier.isFinal(field.getModifiers())) {
                            return;
                        }
                        // 获取对应字段参数值
                        String encryptField = webRequest.getParameter(fieldName);
                        String objectParameterValue = encryptField;
                        boolean isDecryptParam = field.isAnnotationPresent(DecryptParam.class);
                        boolean isDecryptIgnore = field.isAnnotationPresent(DecryptIgnore.class);
                        boolean isDecryptField = SecureApiThreadLocal.getIsDecryptApi() && !isDecryptIgnore || isDecryptParam;
                        if (isDecryptField) {
                            // 解密字段参数值
                            objectParameterValue = CipherModeHandler.handleDecryptMode(objectParameterValue, secureApiPropertiesConfig);
                            showLog(parameterType.getSimpleName() + "." + fieldName, encryptField, objectParameterValue);
                        }
                        // 获取字段类型
                        Class<?> fieldType = field.getType();
                        // 获取字段包装类型
                        Class<?> fieldPackageType = ClassUtils.resolvePrimitiveIfNecessary(fieldType);
                        // 设置对象字段值
                        Object o = getObjectByType(fieldPackageType, objectParameterValue);
                        // 转换String字符串为实体类字段类型
                        if (StringUtils.hasText(objectParameterValue) && o == null) {
                            // 处理日期类型
                            if (fieldPackageType == Date.class || Temporal.class.isAssignableFrom(fieldPackageType)) {
                                o = convertStringAsDate(objectParameterValue, fieldPackageType);
                            } else {
                                Constructor<?> constructor = fieldPackageType.getConstructor(objectParameterValue.getClass());
                                o = constructor.newInstance(objectParameterValue);
                            }
                        }
                        if (o != null) {
                            field.set(result, o);
                        }
                    } catch (Exception e) {
                        log.error("实体类字段：{} 设置出现异常，跳过此字段值的设置", parameterType.getSimpleName() + "." + fieldName, e);
                    }
                }, threadPool));
            }
            clazz = clazz.getSuperclass();
        }
        CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0])).join();
    }

    /**
     * 把string转换为日期
     * @param string 日期字符串
     * @param parameterType 目标类型
     * @return 日期对象
     */
    private Object convertStringAsDate(String string, Class<?> parameterType) {
        if (parameterType == Date.class) {
            for (DateTimeFormatter formatter : dateFormatterList) {
                try {
                    // 尝试解析 LocalDateTime 类型
                    LocalDateTime localDateTime = LocalDateTime.parse(string, formatter);
                    return Date.from(localDateTime.atZone(TimeZone.getDefault().toZoneId()).toInstant());
                } catch (Exception ignored) {
                }
            }
        } else if (parameterType == LocalDateTime.class) {
            for (DateTimeFormatter formatter : dateFormatterList) {
                try {
                    // 尝试解析 LocalDateTime 类型
                    return LocalDateTime.parse(string, formatter);
                } catch (Exception ignored) {
                }
            }
        } else if (parameterType == LocalDate.class) {
            for (DateTimeFormatter formatter : dateFormatterList) {
                try {
                    return LocalDate.parse(string, formatter);
                } catch (Exception ignored) {
                }
            }
        } else if (parameterType == LocalTime.class) {
            for (DateTimeFormatter formatter : dateFormatterList) {
                try {
                    return LocalTime.parse(string, formatter);
                } catch (Exception ignored) {
                }
            }
        } else if (parameterType == OffsetDateTime.class) {
            for (DateTimeFormatter formatter : dateFormatterList) {
                try {
                    return OffsetDateTime.parse(string, formatter);
                } catch (Exception ignored) {
                }
            }
        } else if (parameterType == ZonedDateTime.class) {
            for (DateTimeFormatter formatter : dateFormatterList) {
                try {
                    return ZonedDateTime.parse(string, formatter);
                } catch (Exception ignored) {
                }
            }
        }
        log.error("没有匹配的日期转换格式，原值：{}，目标类型：{}", string, parameterType);
        return null;
    }

    private void showLog(String paramName, String encryptString, String decryptString) {
        if (secureApiPropertiesConfig.isShowLog()) {
            log.info("接口param/formData参数解密，参数名：{}，解密前：{}，解密后：{}", paramName, encryptString, decryptString);
        }
    }

}
