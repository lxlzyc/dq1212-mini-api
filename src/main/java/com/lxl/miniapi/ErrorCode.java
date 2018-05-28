package com.lxl.miniapi;

/**
 * @author: codeyung  E-mail:yjc199308@gmail.com
 * @date: 2017/11/15.下午5:23
 */
public enum ErrorCode {

    OK(200, "接口调用成功"),

    //系统级别（1000-1999）
    SERVER_BUSY(1000, "系统繁忙，此时请开发者稍候再试")
    , ERROR(1001, "错误")
    , SERVER_NOTFOUND(1002, "服务器错误")
    , PARAM_ERROR(10003, "请求参数异常")
    , SIGNATURE_FAILURE(10004, "签名验证失败")
    , HIGH_FREQUENCY(10005, "请求频率或数量超过限制")
    , OVER_LENGTH(10006, "请求长度超过限制")
    , REPLAY_ATTACK(10007, "重放攻击");


    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.valueOf(name());
    }
}
