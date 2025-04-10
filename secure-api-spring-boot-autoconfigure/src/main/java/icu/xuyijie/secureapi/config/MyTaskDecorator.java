package icu.xuyijie.secureapi.config;

import icu.xuyijie.secureapi.threadlocal.SecureApiThreadLocal;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;

/**
 * @author 徐一杰
 * @date 2023/8/17 11:22
 * @description 线程数据传递
 */
public class MyTaskDecorator implements TaskDecorator {
    @NonNull
    @Override
    public Runnable decorate(@NonNull Runnable runnable) {
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
