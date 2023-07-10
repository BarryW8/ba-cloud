package com.ba.exception;

import com.ba.response.ResEnum;
import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class ServiceException extends RuntimeException {

    private int code;
    private String msg;

    public ServiceException(String msg) {
        this(ResEnum.ACCOUNT_EXCEPTION.getCode(), msg);
    }

    public ServiceException(int code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public ServiceException(ResEnum codeEnum){
        this(codeEnum.getCode(), codeEnum.getMsg());
    }

}
