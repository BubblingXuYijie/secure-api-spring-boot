package icu.xuyijie.secureapi.config;

import icu.xuyijie.secureapi.threadlocal.SecureApiThreadLocal;
import jakarta.annotation.Nonnull;
import org.springframework.core.task.TaskDecorator;

/**
 * @author 徐一杰
 * @date 2023/8/17 11:22
 * @description 线程数据传递
 */
public class MyTaskDecorator implements TaskDecorator {
    @Nonnull
    @Override
    public Runnable decorate(@Nonnull Runnable runnable) {
        boolean isEncryptApi = SecureApiThreadLocal.getIsEncryptApi();
        boolean isDecryptApi = SecureApiThreadLocal.getIsDecryptApi();
        return () -> {
            try{
                SecureApiThreadLocal.setIsEncryptApi(isEncryptApi);
                SecureApiThreadLocal.setIsDecryptApi(isDecryptApi);
                runnable.run();
            } finally {
                SecureApiThreadLocal.clearIsEncryptApi();
                SecureApiThreadLocal.clearIsDecryptApi();
            }
        };
    }
}
