package com.shsxt.xmjf.api.exception;

import com.shsxt.xmjf.api.constants.XmjfConstant;

public class NoLoginException extends RuntimeException {

    private static final long serialVersionUID = 2801578305815848848L;
    private Integer code = 400;
    private String msg = "用户未登录";

    public NoLoginException() {
        super(XmjfConstant.OPS_FAILED_MSG);
    }

    public NoLoginException(Integer code) {
        super(XmjfConstant.OPS_FAILED_MSG);
        this.code = code;
    }

    public NoLoginException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public NoLoginException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
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
}
