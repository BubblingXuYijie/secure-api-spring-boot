package icu.xuyijie.secureapi.interceptor;

import icu.xuyijie.secureapi.threadlocal.SecureApiThreadLocal;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 徐一杰
 * @date 2024/6/21 15:20
 * @description 根据请求路径判断是否加密接口
 */
@Component
public class SecureApiEncryptPathInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        SecureApiThreadLocal.setIsEncryptApi(true);
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) throws Exception {
        SecureApiThreadLocal.clearIsEncryptApi();
    }
}
