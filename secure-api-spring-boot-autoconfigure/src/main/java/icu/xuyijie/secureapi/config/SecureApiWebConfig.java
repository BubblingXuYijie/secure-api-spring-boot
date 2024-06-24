package icu.xuyijie.secureapi.config;

import icu.xuyijie.secureapi.handler.SecureApiArgumentResolver;
import icu.xuyijie.secureapi.interceptor.SecureApiDecryptPathInterceptor;
import icu.xuyijie.secureapi.interceptor.SecureApiEncryptPathInterceptor;
import icu.xuyijie.secureapi.model.SecureApiProperties;
import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author 徐一杰
 * @date 2024/6/20 17:13
 * @description WebMvc配置
 */
@SpringBootConfiguration
public class SecureApiWebConfig implements WebMvcConfigurer {
    private final SecureApiEncryptPathInterceptor secureApiEncryptPathInterceptor;
    private final SecureApiDecryptPathInterceptor secureApiDecryptPathInterceptor;
    private final SecureApiArgumentResolver secureApiArgumentResolver;
    private final SecureApiPropertiesConfig secureApiPropertiesConfig;

    public SecureApiWebConfig(SecureApiEncryptPathInterceptor secureApiEncryptPathInterceptor, SecureApiDecryptPathInterceptor secureApiDecryptPathInterceptor, SecureApiArgumentResolver secureApiArgumentResolver, SecureApiPropertiesConfig secureApiPropertiesConfig) {
        this.secureApiEncryptPathInterceptor = secureApiEncryptPathInterceptor;
        this.secureApiDecryptPathInterceptor = secureApiDecryptPathInterceptor;
        this.secureApiArgumentResolver = secureApiArgumentResolver;
        this.secureApiPropertiesConfig = secureApiPropertiesConfig;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 加载加密url拦截器
        addSecureApiPathInterceptor(registry, secureApiEncryptPathInterceptor, secureApiPropertiesConfig.getEncryptUrl());
        // 加载解密url拦截器
        addSecureApiPathInterceptor(registry, secureApiDecryptPathInterceptor, secureApiPropertiesConfig.getDecryptUrl());
    }

    private void addSecureApiPathInterceptor(InterceptorRegistry registry, HandlerInterceptor interceptor, SecureApiProperties.UrlPattern urlPattern) {
        // 确保includeUrls数组不为null
        List<String> includeUrls = Optional.ofNullable(urlPattern)
                .map(SecureApiProperties.UrlPattern::getIncludeUrls)
                .orElse(new ArrayList<>());
        // 用户配置了includeUrls才加载拦截器
        if (!includeUrls.isEmpty()) {
            // 确保excludeUrls数组不为null
            List<String> excludeUrls = Optional.of(urlPattern)
                    .map(SecureApiProperties.UrlPattern::getExcludeUrls)
                    .orElse(new ArrayList<>());
            registry.addInterceptor(interceptor)
                    .addPathPatterns(includeUrls)
                    .excludePathPatterns(excludeUrls);
        }
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(secureApiArgumentResolver);
    }
}
