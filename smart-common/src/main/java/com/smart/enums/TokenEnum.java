package com.smart.enums;

public enum TokenEnum {
    //  1:app,2:公众号，3:订阅号、4:小程序，
    //APP("1", "巡检app", 60 * 60 * 24 * 7L),
    APP("1", "巡检app", 60 * 30L),
    OFFICIAL_ACCOUNT("2", "公众号", 60 * 60 * 24 * 7L),
    APPLET("4", "小程序", 60 * 60 * 24L),
    WEB("5", "web", 60 * 30L),
    CARER_APP("6", "车主APP", 60 * 60 * 24 * 7L),
    ;
    String code;
    String name;
    //单位:秒
    Long time;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Long getTime() {
        return time;
    }

    TokenEnum(String code, String name, Long time) {
        this.code = code;
        this.name = name;
        this.time = time;
    }

}
