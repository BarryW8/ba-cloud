package com.ba.token;

import com.ba.exception.ServiceException;
import com.ba.response.ResEnum;

import java.util.Arrays;
import java.util.Optional;

public enum TokenEnum {
    WEB("1", "Web", 60 * 30L),
    APP("2", "App", 60 * 60 * 24 * 7L),
    APPLET("3", "小程序", 60 * 60 * 24L),
    ;

    String code;
    String name;
    Long time; //单位:秒

    TokenEnum(String code, String name, Long time) {
        this.code = code;
        this.name = name;
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Long getTime() {
        return time;
    }

    public static TokenEnum getEnum(String gCode) {
        Optional<TokenEnum> resultOps = Arrays.stream(TokenEnum.values()).filter(t -> t.code.equals(gCode)).findFirst();
        if (resultOps.isPresent()) {
            return resultOps.get();
        }
        throw new ServiceException(ResEnum.SYSTEM_REQUEST_ERROR);
    }

}
