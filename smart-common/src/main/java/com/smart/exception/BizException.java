package com.smart.exception;

import lombok.Data;

/**
 * 业务异常
 **/
@Data
public class BizException extends RuntimeException {

    private int code;
    private String msg;

    public BizException(int code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
