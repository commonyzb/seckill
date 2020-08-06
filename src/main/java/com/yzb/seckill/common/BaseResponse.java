package com.yzb.seckill.common;

/**
 * @author: yzb
 * @date: 2020/8/3 21:21
 * @package: com.yzb.seckill.common
 * @description: 统一响应封装
 * @version: 1.0
 */
public class BaseResponse<T> {

    private Integer code;
    private String message;
    private T data;

    public BaseResponse() {
    }

    public BaseResponse(Integer code, String message) {
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

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
