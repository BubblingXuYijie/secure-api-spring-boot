package icu.xuyijie.exception;

import icu.xuyijie.entity.ResultEntity;
import icu.xuyijie.secureapi.annotation.EncryptApi;
import icu.xuyijie.secureapi.exception.SecureApiException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 徐一杰
 * @date 2022/11/14 11:19
 * @description 自定义全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 可以自定义处理SecureApi异常
     *
     * @param request 请求信息
     * @param e       SecureApi异常
     * @return 自定义返回体，这里也可以使用@EncryptApi进行返回结果加密
     */
    @ExceptionHandler(value = SecureApiException.class)
    //@EncryptApi
    public ResultEntity<Object> bizExceptionHandler(HttpServletRequest request, SecureApiException e) {
        logger.error("SecureApi异常：{}", e.getErrorMsg());
        return ResultEntity.failure(500, e.getErrorMsg());
    }
}
