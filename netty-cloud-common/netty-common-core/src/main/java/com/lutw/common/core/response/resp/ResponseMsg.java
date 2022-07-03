package com.lutw.common.core.response.resp;

import java.io.Serializable;

/**
 * @ClassName ResponseMsg
 * @Description 返回数据结果
 * @Author ltw
 * @Date 2021-01-15 10:40
 * @Version V1.0
 */
public class ResponseMsg implements Serializable {

    private final static boolean SUCCESS = true;

    private final static boolean ERROR = true;

    public static <T> ResponseResult<T> success() {
        return new ResponseResult<T>().setCode(ResultCode.SUCCESS).setSuccess(SUCCESS);
    }

    public static <T> ResponseResult<T> success(String message) {
        return new ResponseResult<T>().setCode(ResultCode.SUCCESS).setMsg(message).setSuccess(SUCCESS);
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<T>().setCode(ResultCode.SUCCESS).setSuccess(SUCCESS).setData(data);
    }

    public static <T> ResponseResult<T> success(Boolean flag) {
        Long code;
        code = flag ? ResultCode.SUCCESS : ResultCode.ERROR;
        return new ResponseResult<T>().setCode(code).setSuccess(flag);
    }

    public static <T> ResponseResult<T> success(String message, T data) {
        return new ResponseResult<T>().setCode(ResultCode.SUCCESS).setMsg(message).setData(data).setSuccess(SUCCESS);
    }

    public static <T> ResponseResult<T> success(Long code,Boolean success, String message, T data) {
        return new ResponseResult<T>().setCode(code).setSuccess(success).setMsg(message).setData(data);
    }

    public static <T> ResponseResult<T> error() {
        return new ResponseResult<T>().setCode(ResultCode.ERROR).setSuccess(ERROR);
    }

    public static <T> ResponseResult<T> error(String message) {
        return new ResponseResult<T>().setCode(ResultCode.ERROR).setSuccess(SUCCESS).setMsg(message);
    }

    public static <T> ResponseResult<T> error(Long code, String message) {
        return new ResponseResult<T>().setCode(code).setSuccess(SUCCESS).setMsg(message);
    }

    public static <T> ResponseResult<T> message(Boolean flag) {
        Long code;
        code = flag ? ResultCode.SUCCESS : ResultCode.ERROR;
        return new ResponseResult<T>().setCode(code).setSuccess(flag);
    }
}
