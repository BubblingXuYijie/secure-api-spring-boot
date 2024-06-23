package icu.xuyijie.entity;

/**
 * @author 徐一杰
 * @date 2021/10/27
 * @description 通用返回体
 */
public class ResultEntity<T> {
    private Integer code;
    private String message;
    private T data;

    public ResultEntity() {

    }

    public ResultEntity(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResultEntity(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ResultEntity<T> success(String message) {
        return new ResultEntity<>(200, message);
    }

    public static <T> ResultEntity<T> success(String message, T data) {
        return new ResultEntity<>(200, message, data);
    }

    public static <T> ResultEntity<T> failure(Integer code, String message) {
        return new ResultEntity<>(code, message);
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
