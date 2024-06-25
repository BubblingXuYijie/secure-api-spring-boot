package icu.xuyijie.secureapi.handler;

import icu.xuyijie.secureapi.annotation.DecryptParam;
import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;
import icu.xuyijie.secureapi.threadlocal.SecureApiThreadLocal;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.*;

/**
 * @author 徐一杰
 * @date 2024/6/24 19:14
 * @description param参数处理器
 */
@Component
public class SecureApiArgumentResolver implements HandlerMethodArgumentResolver {
    private final SecureApiPropertiesConfig secureApiPropertiesConfig;

    public SecureApiArgumentResolver(SecureApiPropertiesConfig secureApiPropertiesConfig) {
        this.secureApiPropertiesConfig = secureApiPropertiesConfig;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean parameterAnnotation = parameter.hasParameterAnnotation(DecryptParam.class);
        // 三种情况走这个处理器：1、配置了解密url并且没有加@RequestParam注解。2、参数加了DecryptParam注解
        return SecureApiThreadLocal.getIsDecryptApi() || parameterAnnotation;
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
        // 得到参数参数值
        String parameterValue = webRequest.getParameter(parameterName);
        // 获取参数类型
        Class<?> parameterType = parameter.getParameterType();
        // 参数值为null并且DecryptParam注解要求参数必传并且没有设置defaultValue，抛出异常
        if (parameterValue == null && hasDecryptParam && decryptParam.required() && !StringUtils.hasText(decryptParam.defaultValue())) {
            throw new MissingServletRequestParameterException(decryptParam.value(), parameterType.getTypeName());
        }
        // 解密参数值
        parameterValue = CipherModeHandler.handleDecryptMode(parameterValue, secureApiPropertiesConfig);
        // 参数不为空并且解密成功，解密后要自行处理各种类型
        Object result = null;
        if (StringUtils.hasText(parameterValue)) {
            if (List.class.isAssignableFrom(parameterType)) {
                result = Arrays.asList(handleListString(parameterValue));
            } else if (Set.class.isAssignableFrom(parameterType)) {
                result = new HashSet<>(Arrays.asList(handleListString(parameterValue)));
            } else if (Queue.class.isAssignableFrom(parameterType)) {
                result = new PriorityQueue<>(Arrays.asList(handleListString(parameterValue)));
            } else if (Map.class.isAssignableFrom(parameterType)) {
                String[] kvs = parameterValue.replace("\"", "").replace("{", "").replace("}", "").replace(" ", "").split(",");
                Map<String, String> map = new HashMap<>(kvs.length);
                for (String kv : kvs) {
                    String[] split = kv.split("=");
                    map.put(split[0], split[1]);
                }
                result = map;
            }
        } else {
            // 参数解密失败或者参数为空不解密返回defaultValue
            if (hasDecryptParam && StringUtils.hasText(decryptParam.defaultValue())) {
                result = decryptParam.defaultValue();
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
        return s.replace("[", "").replace("]", "").replace(" ", "").split(",");
    }

}
