package icu.xuyijie.interceptor;

import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author 徐一杰
 * @date 2024/6/20
 * @description 拦截器
 */
@Component
public class SecureKeyInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(SecureKeyInterceptor.class);
    private final SecureApiPropertiesConfig secureApiPropertiesConfig;

    public SecureKeyInterceptor(SecureApiPropertiesConfig secureApiPropertiesConfig) {
        this.secureApiPropertiesConfig = secureApiPropertiesConfig;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        // 这里可以使用会话密钥，只需要在拦截器中修改secureApiPropertiesConfig的key值
        String sessionKey = request.getHeader("sessionKey");
        log.info("前端传来会话密钥：{}", sessionKey);
        secureApiPropertiesConfig.setKey(sessionKey);
        return true;
    }
}
