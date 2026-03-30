package pn.wen.playeradmin.common;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一模型
 * @param <T>
 */
public class FlyerResult<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 666L;
    private Integer code;
    private String msg;
    private Boolean success;
    private T data;

    public static <T> FlyerResult<T> success(T data) {
        return success(0, "成功", data);
    }

    public static <T> FlyerResult<T> success() {
        return success(0, "成功", null);
    }

    public static <T> FlyerResult<T> success(String msg) {
        return success(0, msg, null);
    }

    public static <T> FlyerResult<T> success(String msg, T data) {
        return success(0, msg, data);
    }

    public static <T> FlyerResult<T> success(Integer code, String msg, T data) {
        FlyerResult<T> res = new FlyerResult<>();
        res.code = code;
        res.msg = msg;
        res.data = data;
        res.success = Boolean.TRUE;
        return res;
    }

    public static <T> FlyerResult<T> fail(Integer code, String msg) {
        return fail(code, msg, null);
    }

    public static <T> FlyerResult<T> fail(Integer code, String msg, T data) {
        FlyerResult<T> res = new FlyerResult<>();
        res.code = code;
        res.msg = msg;
        res.data = data;
        res.success = Boolean.FALSE;
        return res;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
