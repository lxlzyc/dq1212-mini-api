package com.lxl.miniapi;

import java.util.HashMap;

/**
 * @author: codeyung  E-mail:yjc199308@gmail.com
 * @date: 2017/11/15.下午5:23
 */
public class Result extends HashMap<Object, Object> {


    public void setMessage(String message) {
        this.put("message", message);
    }

    public Result() {
        super();
        this.setSuccess(false);
    }

    public Result(boolean success) {
        super();
        this.setSuccess(success);
    }

    public void setSuccess(boolean success) {
        if (false == success) {
            this.put("code", ErrorCode.ERROR.getCode());
            this.put("msg", ErrorCode.ERROR);
            this.put("message", ErrorCode.ERROR.getMessage());
        } else {
            this.put("code", ErrorCode.OK.getCode());
            this.put("msg", ErrorCode.OK);
            this.put("message", ErrorCode.OK.getMessage());
        }
    }

    public void setError(ErrorCode code) {
        this.put("code", code.getCode());
        this.put("msg", code);
        this.put("message", code.getMessage());
    }

}
