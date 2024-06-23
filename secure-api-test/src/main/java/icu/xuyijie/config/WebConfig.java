package icu.xuyijie.config;

import icu.xuyijie.interceptor.SecureKeyInterceptor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 徐一杰
 * @date 2024/6/20 17:13
 * @description
 */
@SpringBootConfiguration
public class WebConfig implements WebMvcConfigurer {
    private final SecureKeyInterceptor secureKeyInterceptor;

    public WebConfig(SecureKeyInterceptor secureKeyInterceptor) {
        this.secureKeyInterceptor = secureKeyInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(secureKeyInterceptor)
                .addPathPatterns("/**");
    }

}
