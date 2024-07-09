package icu.xuyijie.secureapi.handler;

import icu.xuyijie.secureapi.annotation.DecryptParam;
import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;
import icu.xuyijie.secureapi.threadlocal.SecureApiThreadLocal;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author 徐一杰
 * @date 2024/6/24 19:14
 * @description param和formData参数处理器
 */
@Component
public class SecureApiArgumentResolver implements HandlerMethodArgumentResolver {
    private final SecureApiPropertiesConfig secureApiPropertiesConfig;

    public SecureApiArgumentResolver(SecureApiPropertiesConfig secureApiPropertiesConfig) {
        this.secureApiPropertiesConfig = secureApiPropertiesConfig;
    }

    @Override
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        boolean parameterAnnotation = parameter.hasParameterAnnotation(DecryptParam.class);
        // 获取参数类型
        Class<?> parameterType = parameter.getParameterType();
        // 根据参数类型，获取对象，如果对象为空，可能是实体类，放行，下面会检测实体类字段哪些需要解密
        Object o = getObjectByType(parameterType, "");
        // 三种情况走这个处理器：1、配置了解密url并参数没有加@RequestParam、@RequestPart注解。2、参数加了@DecryptParam注解。3、参数是实体类
        // 即使在 SecureApi 的 enable 设置为 false 也会走这个处理器，可以保留 @DecryptParam 的基本解析功能，目前是这样设计的
        return SecureApiThreadLocal.getIsDecryptApi() || parameterAnnotation || o == null;
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
        String parameterValue = webRequest.getParameter(parameterName);
        // 获取参数类型
        Class<?> parameterType = parameter.getParameterType();
        // 参数值为null并且DecryptParam注解要求参数必传并且没有设置defaultValue，抛出异常
        if (parameterValue == null && hasDecryptParam && decryptParam.required() && !StringUtils.hasText(decryptParam.defaultValue())) {
            throw new MissingServletRequestParameterException(decryptParam.value(), parameterType.getTypeName());
        }
        // 解密参数值，这里再判断一次是应对实体类作为参数的情况，参数是实体类时 @DecryptParam 注解夹在字段上，无法在 supportsParameter 方法中判断，只能进入到这里判断
        boolean isDecryptParam = (SecureApiThreadLocal.getIsDecryptApi() || hasDecryptParam) && secureApiPropertiesConfig.isEnabled();
        if (isDecryptParam) {
            parameterValue = CipherModeHandler.handleDecryptMode(parameterValue, secureApiPropertiesConfig);
        }
        // 参数不为空并且解密成功，解密后要自行处理各种类型
        Object result = null;
        if (parameterValue != null) {
            // 特点参数类型需要处理成对象，不需要处理的类型保持原值
            result = getObjectByType(parameterType, parameterValue);
            if (result == null) {
                result = parameterValue;
            }
        } else {
            // 对象类型情况
            try {
                // 获取对象
                result = parameterType.getDeclaredConstructor().newInstance();
                // 获取对象内字段
                for (Field field : parameterType.getDeclaredFields()) {
                    // 获取对应字段参数值
                    String objectParameterValue = webRequest.getParameter(field.getName());
                    boolean fieldAnnotationPresent = field.isAnnotationPresent(DecryptParam.class);
                    boolean isDecryptField = (fieldAnnotationPresent || SecureApiThreadLocal.getIsDecryptApi()) && secureApiPropertiesConfig.isEnabled();
                    if (isDecryptField) {
                        // 解密字段参数值
                        objectParameterValue = CipherModeHandler.handleDecryptMode(objectParameterValue, secureApiPropertiesConfig);
                    }
                    // 获取字段类型
                    Class<?> fieldType = field.getType();
                    // 获取字段包装类型
                    Class<?> fieldPackageType = ClassUtils.resolvePrimitiveIfNecessary(fieldType);
                    // 设置对象字段值
                    Object o = getObjectByType(fieldPackageType, objectParameterValue);
                    if (StringUtils.hasText(objectParameterValue) && o == null) {
                        // 转换String类型为实体类字段类型
                        Constructor<?> constructor = fieldPackageType.getConstructor(objectParameterValue.getClass());
                        o = constructor.newInstance(objectParameterValue);
                    }
                    field.setAccessible(true);
                    field.set(result, o);
                }
            } catch (Exception e) {
                // 参数解密失败或者参数为空不解密返回defaultValue
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
    private static String[] handleListString(String s) {
        String replace = s.replace("[", "").replace("]", "").replace(" ", "");
        if (StringUtils.hasText(replace)) {
            return replace.split(",");
        }
        return new String[0];
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
            o = new ArrayList<>(Arrays.asList(handleListString(parameterValue)));
        } else if (Set.class.isAssignableFrom(parameterType)) {
            o = new HashSet<>(Arrays.asList(handleListString(parameterValue)));
        } else if (Queue.class.isAssignableFrom(parameterType)) {
            o = new PriorityQueue<>(Arrays.asList(handleListString(parameterValue)));
        } else if (Map.class.isAssignableFrom(parameterType)) {
            String[] kvs = parameterValue.replace("\"", "").replace("{", "").replace("}", "").replace(" ", "").split(",");
            if (kvs.length > 0 && StringUtils.hasText(kvs[0])) {
                Map<String, String> map = new HashMap<>(kvs.length);
                for (String kv : kvs) {
                    String[] split = kv.split("=");
                    map.put(split[0], split[1]);
                }
                o = map;
            } else {
                o = new HashMap<>(0);
            }
        }
        return o;
    }

}
