package icu.xuyijie.secureapi.exception;

/**
 * @author 徐一杰
 * @date 2024/6/19 11:21
 * @description SecureApi异常
 */
public class SecureApiException extends RuntimeException {
    /**
     * 错误信息
     */
    private final String errorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }

    public SecureApiException() {
        super();
        this.errorMsg = ErrorEnum.FAILURE.getMessage();
    }

    public SecureApiException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.errorMsg = errorEnum.getMessage();
    }

    public SecureApiException(ErrorEnum errorEnum, Throwable cause) {
        super(errorEnum.getMessage(), cause);
        this.errorMsg = errorEnum.getMessage();
    }

    public SecureApiException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public SecureApiException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorMsg = errorMsg;
    }
}
